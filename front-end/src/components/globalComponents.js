import single from '@/components/single'
import custom from '@/components/custom'
import inputFielda from "./InputFielda.vue";

function plugin(Vue) {
  if(plugin.installed){
    return
  }
  Vue.component('singleSelect',single)
  Vue.component('customMultipleInput',custom)
  Vue.component('inputFielda',inputFielda)
};

export default plugin
