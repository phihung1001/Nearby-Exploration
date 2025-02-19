import { useState } from "react";

export default function useMutation({ mutationFn, options }) {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [data, setData] = useState(null);

  const mutate = async (...params) => {
    setData(null);
    setIsLoading(true);
    setError(null);

    try {
      const result = await mutationFn(...params);
      setData(result);
      options?.onSuccess?.(result);
      return result;
    } catch (err) {
      const customError = err instanceof Error ? err : new Error("An error occurred");
      setError(customError);
      options?.onError?.(customError);
    } finally {
      setIsLoading(false);
    }
  };

  return { isLoading, error, data, mutate, success: !!data };
}
