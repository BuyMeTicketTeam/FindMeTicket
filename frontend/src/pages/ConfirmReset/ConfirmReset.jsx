/* eslint-disable no-nested-ternary */
import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useForm } from "react-hook-form";

import {
  useNewPasswordMutation,
  useResetMutation,
} from "../../services/userApi";
import useTimeout from "../../hook/useTimeout";
import { CODE_PATTERN, PASSWORD_PATTERN } from "../../constants/regex";

import Input from "../../common/Input/Input";
import SuccessPopup from "../../common/SuccessPopup/SuccessPopup";

export default function ConfirmReset() {
  const [showPassword, setShowPassword] = useState();
  const { state } = useLocation();
  const { minutes, seconds, restart } = useTimeout(1, 30);
  const [newPassword, { isSuccess, isLoading, isError, error }] =
    useNewPasswordMutation();
  const [resendResetToken, { isLoading: isResendLoading }] = useResetMutation();
  const { t } = useTranslation("translation", { keyPrefix: "confirmReset" });
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm({ mode: "onTouched" });

  const resendButtonIsDisabled =
    minutes > 0 || seconds > 0 || isSuccess || isResendLoading;

  function passwordValidation(data) {
    if (data.password !== data.confirmPassword) {
      setError("confirmPassword", {
        type: "manual",
      });
      return false;
    }
    return true;
  }

  async function handleResendToken() {
    try {
      const payload = {
        email: state.email,
      };
      await resendResetToken(payload).unwrap();
      restart();
    } catch (err) {
      console.error({ error: err });
    }
  }

  async function onSubmit(data) {
    if (!passwordValidation(data)) {
      return;
    }
    try {
      const payload = {
        code: data.code,
        email: state.email,
        password: data.password,
        confirmPassword: data.confirmPassword,
      };
      await newPassword(payload).unwrap();
    } catch (err) {
      console.error({ error: err });
    }
  }

  return (
    <div className="block-center main">
      <form className="block-center__body" onSubmit={handleSubmit(onSubmit)}>
        <h1 className="block-center__title">{t("title")}</h1>
        {isSuccess && <SuccessPopup t={t} />}
        <p className="block-center__text">{t("confirm_text1")}</p>
        <p className="block-center__text margin_bottom">
          <b>{t("confirm_text2")}</b>
        </p>
        {isError && (
          <p data-testid="error" className="error">
            {t([`error_${error.status}`, "error_500"])}
          </p>
        )}

        <Input
          id="code"
          error={errors.code}
          errorMessage={t("error_400")}
          label={t("code")}
          otherProps={{
            ...register("code", { required: true, pattern: CODE_PATTERN }),
          }}
        />

        <Input
          id="password"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.password}
          errorMessage={t("password_error")}
          label={t("password")}
          otherProps={{
            ...register("password", {
              required: true,
              pattern: PASSWORD_PATTERN,
            }),
          }}
        />

        <Input
          id="confirmPassword"
          showPassword={showPassword}
          setShowPassword={setShowPassword}
          error={errors.confirmPassword}
          errorMessage={t("confirm_password_error")}
          label={t("confirm_password")}
          otherProps={{ ...register("confirmPassword", { required: true }) }}
        />

        <button
          disabled={isLoading || isSuccess}
          className="button"
          type="submit"
        >
          {isLoading ? t("processing") : t("send")}
        </button>

        <button
          data-testid="confirm-send-btn"
          className="button type_link"
          disabled={resendButtonIsDisabled}
          onClick={handleResendToken}
          type="button"
        >
          {isResendLoading
            ? t("processing")
            : minutes === 0 && seconds === 0
            ? t("send_letter")
            : t("time", { minutes, seconds })}
        </button>
      </form>
    </div>
  );
}
