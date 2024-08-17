/* eslint-disable no-param-reassign */
import axios from "axios";

const api = axios.create({
  // baseURL: process.env.REACT_APP_SERVER_ADDRESS,
  baseURL: "https://localhost:8085",
  withCredentials: true,
});

api.interceptors.request.use(
  (requestConfig) => {
    // localStorage.setItem("JWTtoken", "dsadasdadasdasdsa");
    const language = localStorage.getItem("lang") || "eng";
    const cookiesAccepted = localStorage.getItem("acceptedCookies") || false;
    const token = localStorage.getItem("JWTtoken") || "";

    requestConfig.headers["Content-Language"] = language.toLowerCase();

    if (cookiesAccepted) {
      requestConfig.headers["Cookies-Accepted"] = "true";
    }

    if (token) {
      requestConfig.headers.Authorization = token;
    }

    console.log(requestConfig);

    return requestConfig;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => {
    if (response.headers.has("Authorization")) {
      localStorage.setItem("JWTtoken", response.headers.get("Authorization"));
    }
    console.log(response);
    return response;
  },
  (error) => Promise.reject(error)
);

export default api;
