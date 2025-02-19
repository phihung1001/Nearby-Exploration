import { useEffect } from 'react';
import { useForm } from 'react-hook-form';

export default function useAppForm(props = {}) {
  const form = useForm({
    mode: 'onSubmit',
    reValidateMode: 'onSubmit',
    ...props,
  });

  const { clearErrors, watch } = form;

  useEffect(() => {
    const subscription = watch((_values, { name }) => {
      if (name) {
        clearErrors(name);
      }
    });

    return () => subscription.unsubscribe();
  }, [clearErrors, watch]);

  return form;
}
