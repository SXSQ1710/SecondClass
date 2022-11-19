export const setUser = (formdata) => {
  if (formdata != null && formdata.uid) {
    sessionStorage.setItem("uid", formdata.uid);
  }
};
