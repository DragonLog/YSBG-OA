<template>
  <div>
    <div>账号切换</div>
    <button @click="zhangsan()" type="default" size="mini">张三</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <button @click="zzjl()" type="default" size="mini">张总经理</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwM7YwMbQ01lEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQDiCJAqQgAAAA.BQMci7WsE2jWLLs9MBc-JTuzE0X8h0ATRfy9ZAHtnS9qJUf2onPMFTuy3JgpMJ6V7aQVkF8wqp9GY5FjtaQC2Q'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zhangsan() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwM7YwMTWw1FEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSjZVqAb4aTuxDAAAA.82scbDQGjfMnvNnrpyTj8dSwPHP4NTnh0odL503G65jCCEqUdaDNwmBYXdQNM68HuPTHusS5Om4O4PQsYjlS0Q'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMzI2NrMw1FEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSjZVqARpm5KNDAAAA.7W_TkyQDtKf5q92uhoWrHn1suhMtKM0snVqfk_QB6IscPYl48iSw05msJERltP-DSK6WmgvZyZhVKEOiYFEd8Q'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwM7UwNjE30lEqLU4t8kxRsjKBMPMSc1OBWnIyTZRqASGHhChAAAAA.uJqzhSyWCnTYLGrqtwbYzMEGz43F1kjpehOvxH367LCguW2eT8MujtbDSUdBRSpsY_IqGz3wzCGAb0wOt8ErlA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
