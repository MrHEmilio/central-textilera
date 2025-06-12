import { Form, Input, InputRef } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import { NUM_REGEX_LONG10 } from '../../../models/RegularExpression';
import { FormatPhoneMask } from '../../../services/utils';

export default function PhoneInputMask({
  initialValue,
  addonBefore
}: {
  initialValue?: string;
  addonBefore?: React.ReactNode;
}) {
  const [phoneS, setPhoneS] = useState('');

  const phoneRef = useRef<InputRef>(null);

  useEffect(() => {
    if (!initialValue || !phoneRef) return;
    setTimeout(() => {
      if (phoneRef.current?.input) {
        phoneRef.current.input.value = FormatPhoneMask(initialValue);
      }
    }, 700);
  }, [initialValue, phoneRef]);

  useEffect(() => {
    if (phoneRef.current?.input && phoneS) {
      phoneRef.current.input.value = FormatPhoneMask(phoneS);
    }
  }, [phoneS, phoneRef]);

  return (
    <Form.Item
      className="select-phone"
      label="Teléfono"
      name="phone"
      required
      rules={[
        { required: true, message: 'Escriba un teléfono por favor' },
        {
          pattern: new RegExp(NUM_REGEX_LONG10),
          message: 'Escriba un teléfono válido'
        }
      ]}
    >
      <Input
        onBlur={({ target }) => {
          setPhoneS(target.value);
        }}
        ref={phoneRef}
        className="custom-input p-0"
        addonBefore={addonBefore}
        size="large"
        type="tel"
        maxLength={10}
      />
    </Form.Item>
  );
}
