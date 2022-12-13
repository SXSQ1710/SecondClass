import Vuex from "vuex";
const store = new Vuex.Store({
  state: {
    token: "",
  },
  mutations: {
    set_token(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
    },
    del_token(state) {
      state.token = "";
      localStorage.removeItem("token");
    },
  },
});
export default store;
