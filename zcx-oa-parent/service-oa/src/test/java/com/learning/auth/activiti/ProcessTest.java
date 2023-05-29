package com.learning.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void findGroupTaskList() {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("tom01").list();
        for (Task task : list) {
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    @Test
    public void claimTask() {
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("tom01")
                .singleResult();

        if (task != null) {
            taskService.claim(task.getId(), "tom01");
            System.out.println("任务已拾取");
        }
    }

    @Test
    public void SingleSuspendProcessInstance() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("a4b2efa6-f0cf-11ed-8dd7-005056c00001")
                .singleResult();
        if(processInstance.isSuspended()) {
            runtimeService.activateProcessInstanceById("a4b2efa6-f0cf-11ed-8dd7-005056c00001");
            System.out.println("由挂起转为激活");
        } else {
            runtimeService.suspendProcessInstanceById("a4b2efa6-f0cf-11ed-8dd7-005056c00001");
            System.out.println("由激活转为挂起");
        }
    }

    @Test
    public void suspendProcessInstance() {
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("qingjia").singleResult();

        boolean suspended = qingjia.isSuspended();
        if (suspended) {
            repositoryService.activateProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println("由挂起转为激活");
        } else {
            repositoryService.suspendProcessDefinitionById(qingjia.getId(), true, null);
            System.out.println("由激活转为挂起");
        }
    }

    @Test
    public void startUpProcessAddBusinessKey() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getId());
    }

    @Test
    public void findCompleteTaskList() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("zhangsan")
                .finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance.getProcessInstanceId());
            System.out.println(historicTaskInstance.getId());
            System.out.println(historicTaskInstance.getAssignee());
            System.out.println(historicTaskInstance.getName());
        }
    }

    @Test
    public void completeTask() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("gouwa")
                .list();

        for (Task task : tasks) {
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("assignee2", "zhao");
            taskService.complete(task.getId());
        }
    }

    @Test
    public void findTestList() {
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("xiaoli").list();
        for (Task task : list) {
            System.out.println(task.getProcessVariables());
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    @Test
    public void startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban03");
        System.out.println(processInstance.getDeploymentId());
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void deployProcess() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia02.bpmn20.xml")
                .addClasspathResource("process/myfilename-20230514090823804.png")
                .name("请假申请流程02")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());

    }

    @Test
    public void startProcessInstance() {
        Map<String, Object> map = new HashMap<>();
//        map.put("assignee1", "lucy01");
//        map.put("assignee2", "mary01");
//        map.put("day", 3);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia02");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

}
