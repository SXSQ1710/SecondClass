import axios from "@/axios";

export function getMySelfApplication(uid, pageNo, pageSize) {
  return axios.get(
    "/api/selfApplication/getMySelfApplication/{uid}/{pageNo}/{pageSize}"
  );
}
