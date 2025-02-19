import { useState } from "react";

function useLogin({ isAdmin }) {
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async ({ email, password }) => {
    setIsLoading(true);
    try {
      const response = await fetch("/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, isAdmin }),
      });

      const data = await response.json();

      if (data.ok) {
        // Lấy callback URL từ query params (tương đương `searchParams.get("callbackUrl")`)
        const params = new URLSearchParams(window.location.search);
        const callbackUrl = params.get("callbackUrl");

        if (callbackUrl) {
          window.location.href = callbackUrl;
        } else {
          window.location.href = isAdmin ? "/provider/dashboard" : "/dashboard";
        }
      }
    } catch (error) {
      console.error("Login failed:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return { isLoading, onSubmit: handleLogin };
}

export default useLogin;
