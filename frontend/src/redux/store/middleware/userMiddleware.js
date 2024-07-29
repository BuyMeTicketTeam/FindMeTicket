import { createListenerMiddleware, isAnyOf } from "@reduxjs/toolkit";
import Cookies from "universal-cookie";
import { userApi } from "../../../services/userApi";

const userListenerMiddleware = createListenerMiddleware();

const cookies = new Cookies(null, { path: "/" });

userListenerMiddleware.startListening({
  matcher: isAnyOf(
    userApi.endpoints.logout.matchPending,
    userApi.endpoints.deleteUser.matchFulfilled
  ),
  effect: () => {
    localStorage.removeItem("JWTtoken");
    localStorage.removeItem("userData");
    sessionStorage.removeItem("userData");
    cookies.remove("USER_ID");
  },
});

userListenerMiddleware.startListening({
  matcher: isAnyOf(
    userApi.endpoints.login.matchFulfilled,
    userApi.endpoints.loginGoogle.matchFulfilled,
    userApi.endpoints.loginFacebook.matchFulfilled
  ),
  effect: (action) => {
    const userData = {
      isAuthenticated: true,
      username: action.payload.username,
      notification: action.payload.notification,
      userEmail: action.payload.email,
      userPhoto:
        action.payload.googlePicture ??
        `data:image/jpeg;base64,${action.payload.basicPicture}`,
    };
    sessionStorage.setItem("userData", JSON.stringify(userData));
  },
});

function updateNotificationInStorage(notification) {
  const userDataLocal = localStorage.getItem("userData");
  const userDataSession = sessionStorage.getItem("userData");
  if (userDataLocal) {
    const parsedData = JSON.parse(userDataLocal);
    localStorage.setItem(
      "userData",
      JSON.stringify({ ...parsedData, notification })
    );
  }
  if (userDataSession) {
    const parsedData = JSON.parse(userDataSession);
    sessionStorage.setItem(
      "userData",
      JSON.stringify({ ...parsedData, notification })
    );
  }
}

userListenerMiddleware.startListening({
  matcher: userApi.endpoints.notificationDisable.matchFulfilled,
  effect: () => updateNotificationInStorage(false),
});

userListenerMiddleware.startListening({
  matcher: userApi.endpoints.notificationEnable.matchFulfilled,
  effect: () => updateNotificationInStorage(true),
});

export { userListenerMiddleware };
