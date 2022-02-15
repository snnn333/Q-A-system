import Vue from 'vue'
import Router from 'vue-router'
import register from '@/components/register'
import Login from '@/components/Login'
import homeTest from '@/components/homeTest'
import MyHome from '@/components/MyHome'
import AlterInfo from '@/components/AlterInfo'
import AlterPsw from '@/components/AlterPsw'
import QuestionShow from '@/components/QuestionShow'
import QuestionDetail from '@/components/QuestionDetail'
import OperatorUser from '@/components/OperatorUser'
import OperatorClass from '@/components/OperatorClass'
import OperatorLabel from '@/components/OperatorLabel'
import test from '@/components/test'
import confirmRegister from '@/components/confirmRegister'
import resetPsw from '@/components/resetPsw'
import QuestionSubmit from '@/components/QuestionSubmit'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/MyHome',
      name: 'MyHome',
      component:MyHome
    },
    {
      path: '/',
      name: 'homeTest',
      component:homeTest
    },
    {
      path: '/register',
      name: 'register',
      component:register
    },
    {
      path: '/Login',
      name: 'Login',
      component:Login
    },
    {
      path: '/AlterInfo',
      name: 'AlterInfo',
      component:AlterInfo
    },
    {
      path: '/AlterPsw',
      name: 'AlterPsw',
      component:AlterPsw
    },
    {
      path: '/QuestionShow',
      name: 'QuestionShow',
      component: QuestionShow
    },
    {
      path: '/QuestionDetail',
      name: 'QuestionDetail',
      component: QuestionDetail
    },
    {
      path: '/OperatorUser',
      name: 'OperatorUser',
      component: OperatorUser
    },
    {
      path: '/OperatorClass',
      name: 'OperatorClass',
      component: OperatorClass
    },
    {
      path: '/OperatorLabel',
      name: 'OperatorLabel',
      component: OperatorLabel
    },
    {
      path: '/test',
      name: 'test',
      component: test
    },
    {
      path: '/resetPsw',
      name: 'resetPsw',
      component: resetPsw
    },
    {
      path: '/confirmRegister',
      name: 'confirmRegister',
      component:confirmRegister
    },
    {
      path: '/QuestionSubmit',
      name: 'QuestionSubmit',
      component:QuestionSubmit
    },
  ]
})
