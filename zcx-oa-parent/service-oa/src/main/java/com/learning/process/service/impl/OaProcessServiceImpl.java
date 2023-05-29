package com.learning.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.auth.service.SysUserService;
import com.learning.model.process.Process;
import com.learning.model.process.ProcessRecord;
import com.learning.model.process.ProcessTemplate;
import com.learning.model.system.SysUser;
import com.learning.process.mapper.OaProcessMapper;
import com.learning.process.service.MessageService;
import com.learning.process.service.OaProcessRecordService;
import com.learning.process.service.OaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.process.service.OaProcessTemplateService;
import com.learning.security.custom.LoginUserInfoHelper;
import com.learning.vo.process.ApprovalVo;
import com.learning.vo.process.ProcessFormVo;
import com.learning.vo.process.ProcessQueryVo;
import com.learning.vo.process.ProcessVo;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author zcx
 * @since 2023-05-17
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OaProcessRecordService oaProcessRecordService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MessageService messageService;

    @Override
    public Page<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        return pageModel;
    }

    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        ProcessTemplate processTemplate = oaProcessTemplateService.getById(processFormVo.getProcessTemplateId());

        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);

        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        process.setStatus(1);
        baseMapper.insert(process);

        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        Long businessKey = process.getId();
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", map);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, String.valueOf(businessKey), variables);

        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        List<String> nameList = new ArrayList<>();
        for (Task task : taskList) {
            String assigneeName = task.getAssignee();
            SysUser user = sysUserService.getUserByUsername(assigneeName);
            String name = user.getName();
            nameList.add(name);
            messageService.pushPendingMessage(process.getId(), user.getId(), task.getId());
        }

        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待" + StringUtils.join(nameList.toArray(), ",") + "审批");
        baseMapper.updateById(process);

        oaProcessRecordService.record(process.getId(), 1, "发起申请");
    }

    @Override
    public Page<ProcessVo> findPending(Page<Process> pageParam) {
        TaskQuery query = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();

        int begin = (int) ((pageParam.getCurrent() -1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<Task> taskList = query.listPage(begin, size);

        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task : taskList) {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Process process = baseMapper.selectById(Long.parseLong(businessKey));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }

        Page<ProcessVo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize(), query.count());
        page.setRecords(processVoList);
        return page;
    }

    @Override
    public Map<String, Object> show(Long id) {
        Process process = baseMapper.selectById(id);

        QueryWrapper<ProcessRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", id);
        List<ProcessRecord> list = oaProcessRecordService.list(queryWrapper);

        ProcessTemplate processTemplate = oaProcessTemplateService.getById(process.getProcessTemplateId());

        boolean isApprove = false;
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());

        for (Task task : currentTaskList) {
            if (task.getAssignee().equals(LoginUserInfoHelper.getUsername())) {
                isApprove = true;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", list);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);

        return map;
    }

    @Override
    public void approve(ApprovalVo approvalVo) {
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        if (approvalVo.getStatus() == 1) {
            taskService.complete(taskId);
        } else {
            this.endTask(taskId);
        }

        String description = approvalVo.getStatus().intValue() == 1 ? "已通过" : "驳回";
        oaProcessRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), description);

        Process process = baseMapper.selectById(approvalVo.getProcessId());
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(currentTaskList)) {
            List<String> assignList = new ArrayList<>();
            for (Task task : currentTaskList) {
                String assignee = task.getAssignee();
                SysUser sysUser = sysUserService.getUserByUsername(assignee);
                assignList.add(sysUser.getName());
            }
            process.setDescription("等待" + StringUtils.join(assignList.toArray(), ",") + "审批");
            process.setStatus(1);
        } else {
            if (approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成");
                process.setStatus(2);
            } else {
                process.setDescription("审批驳回");
                process.setStatus(-1);
            }
        }
        baseMapper.updateById(process);
        messageService.pushProcessedMessage(process.getId(), process.getUserId(), approvalVo.getStatus());
    }

    @Override
    public Page<ProcessVo> findProcessed(Page<Process> pageParam) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished()
                .orderByTaskCreateTime().desc();

        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();

        List<HistoricTaskInstance> list = query.listPage(begin, size);
        long totalCount = query.count();

        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : list) {
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            QueryWrapper<Process> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_instance_id", processInstanceId);
            Process process = baseMapper.selectOne(queryWrapper);
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVoList.add(processVo);
        }
        Page<ProcessVo> pageModel = new Page<>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    @Override
    public Page<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        Page<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        return pageModel;
    }

    private void endTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventList)) {
            return;
        }

        FlowNode endFlowNode = endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        List originalSequenceFlowList = new ArrayList();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());

        currentFlowNode.getOutgoingFlows().clear();

        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId("newSequenceFlow");
        sequenceFlow.setSourceFlowElement(currentFlowNode);
        sequenceFlow.setTargetFlowElement(endFlowNode);

        List newSequenceFlowList = new ArrayList();
        newSequenceFlowList.add(sequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        taskService.complete(taskId);
    }

    private List<Task> getCurrentTaskList(String id) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(id).list();
        return list;
    }


}
