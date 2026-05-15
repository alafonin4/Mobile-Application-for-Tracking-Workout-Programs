import axios from "axios";

import API_URL from "../constants/api_url";

const apiClient = axios.create({
  baseURL: "http://10.110.84.28:8083",
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
});

const DEFAULT_STATUS_MESSAGES = {
  400: "Запрос содержит некорректные данные.",
  401: "Не удалось выполнить запрос: требуется повторная авторизация или неверные данные.",
  403: "У вас нет доступа для выполнения этого действия.",
  404: "Запрошенные данные не найдены.",
  409: "Запрос конфликтует с текущими данными.",
  422: "Сервис не смог обработать переданные данные.",
  500: "На сервере произошла ошибка. Попробуйте позже.",
  502: "Сервис временно недоступен. Попробуйте позже.",
  503: "Сервис временно недоступен. Попробуйте позже.",
  504: "Сервис долго не отвечает. Попробуйте позже.",
};

export class ApiRequestError extends Error {
  constructor(message, status = null, details = null) {
    super(message);
    this.name = "ApiRequestError";
    this.status = status;
    this.details = details;
  }
}

const isPlainObject = (value) => value !== null && typeof value === "object" && !Array.isArray(value);

const extractMessageFromPayload = (payload) => {
  if (!payload) {
    return null;
  }

  if (typeof payload === "string") {
    const message = payload.trim();
    return message || null;
  }

  if (Array.isArray(payload)) {
    const messages = payload
      .map((item) => extractMessageFromPayload(item))
      .filter(Boolean);
    return messages.length ? messages.join("\n") : null;
  }

  if (isPlainObject(payload)) {
    const directMessage = [payload.message, payload.error, payload.detail, payload.title]
      .map((item) => (typeof item === "string" ? item.trim() : ""))
      .find(Boolean);

    if (directMessage) {
      return directMessage;
    }

    if (payload.errors) {
      return extractMessageFromPayload(payload.errors);
    }

    if (payload.details) {
      return extractMessageFromPayload(payload.details);
    }
  }

  return null;
};

const getDefaultMessageByStatus = (status) => {
  if (status == null) {
    return "Не удалось связаться с сервером. Проверьте подключение и адрес API.";
  }

  if (DEFAULT_STATUS_MESSAGES[status]) {
    return DEFAULT_STATUS_MESSAGES[status];
  }

  if (status >= 500) {
    return DEFAULT_STATUS_MESSAGES[500];
  }

  return "Не удалось выполнить запрос.";
};

const normalizeApiError = (error) => {
  if (error instanceof ApiRequestError) {
    return error;
  }

  const status = error?.response?.status ?? null;
  const details = error?.response?.data ?? null;
  const message =
    extractMessageFromPayload(details) ||
    extractMessageFromPayload(error?.message) ||
    getDefaultMessageByStatus(status);

  return new ApiRequestError(message, status, details);
};

export const apiRequest = async (config) => {
  try {
    const response = await apiClient.request(config);
    return response.data;
  } catch (error) {
    throw normalizeApiError(error);
  }
};

export const apiGet = (url, config = {}) =>
  apiRequest({
    ...config,
    method: "get",
    url,
  });

export const apiPost = (url, data, config = {}) =>
  apiRequest({
    ...config,
    method: "post",
    url,
    data,
  });

export const apiPut = (url, data, config = {}) =>
  apiRequest({
    ...config,
    method: "put",
    url,
    data,
  });

export const apiDelete = (url, config = {}) =>
  apiRequest({
    ...config,
    method: "delete",
    url,
  });

export const getApiErrorMessage = (error, fallback = "Произошла ошибка.") => {
  const message =
    error instanceof ApiRequestError
      ? error.message
      : typeof error?.message === "string"
      ? error.message.trim()
      : "";

  return message || fallback;
};

export const getApiErrorStatus = (error) =>
  error instanceof ApiRequestError ? error.status : error?.response?.status ?? null;
