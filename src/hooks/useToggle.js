import { useCallback, useState } from "react";

export default function useToggle(initialState = false) {
  const [isOn, setIsOn] = useState(initialState);

  const toggle = useCallback(() => {
    setIsOn((prevState) => !prevState);
  }, []);

  const setOn = useCallback(() => {
    setIsOn(true);
  }, []);

  const setOff = useCallback(() => {
    setIsOn(false);
  }, []);

  return { isOn, toggle, setOn, setOff };
}
