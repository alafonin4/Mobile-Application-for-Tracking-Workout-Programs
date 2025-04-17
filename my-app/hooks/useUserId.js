import { useStorageState } from "./useStorageState";

export function useUserId() {
  const [state, setStateRaw, isLoaded] = useStorageState("userId");

  const setState = (value) => {
    if (typeof value === "number") {
      setStateRaw(String(value));
    } else {
      setStateRaw(value);
    }
  };

  const numericUserId =
    typeof state === "string" && !isNaN(Number(state)) ? Number(state) : null;

  return [numericUserId, setState, isLoaded];
}
