import Vuex from "vuex";
import jscookie from "js-cookie";
import { getTokenName } from "../server/api/saToken";
const tokenName = getTokenName();
const store = new Vuex.Store({
  state: {
    token: "", // 会随着页面刷新而消失
  },
  mutations: {
    set_token(state, token) {
      state.token = token;
      sessionStorage.setItem(tokenName, token);
      jscookie.set(tokenName, token);
    },
    del_token(state) {
      state.token = "";
      sessionStorage.removeItem(tokenName);
      jscookie.remove(tokenName);
    },
  },
});
export default store;
