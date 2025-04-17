import {useEffect, useCallback, useReducer} from 'react';
import { useState} from "react";
import * as SecureStore from 'expo-secure-store';

function useAsyncState(initialValue = [true, null]) {
  return useReducer(
    (state, action) => [false, action],
    initialValue,
    undefined
  );
}

export async function setStorageItemAsync(key, value) {
  if (value == null) {
    await SecureStore.deleteItemAsync(key);
  } else {
    await SecureStore.setItemAsync(key, value);
  }
}

export function useStorageState(key) {
  const [state, setState] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    SecureStore.getItemAsync(key).then((value) => {
      setState(value);
      setIsLoaded(true);
    });
  }, [key]);

  const setValue = useCallback(
    (value) => {
      setState(value);
      if (value === null) {
        SecureStore.deleteItemAsync(key);
      } else {
        SecureStore.setItemAsync(key, value);
      }
    },
    [key]
  );

  return [state, setValue, isLoaded];
}