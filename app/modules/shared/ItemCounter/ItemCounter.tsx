import React, { FC, useEffect, useState } from 'react';
import { Button } from 'antd';
import { NUM_REGEX } from '../../../models/RegularExpression';

interface Props {
  quantity: number;
  maxAmount?: number;
  // eslint-disable-next-line no-unused-vars
  currentQuantity: (arg0: number, arg1: boolean) => void;
}

export const ItemCounter: FC<Props> = ({
  quantity,
  currentQuantity
  // maxAmount
}) => {
  const [useQuantity, setUseQuantity] = useState(0);

  useEffect(() => {
    setUseQuantity(quantity);
  }, []);

  const onClick = (signo: string) => {
    if (signo === '+') {
      setUseQuantity(useQuantity + 1);
      currentQuantity(useQuantity + 1, false);
    } else if (signo === '-' && useQuantity > 1) {
      setUseQuantity(useQuantity - 1);
      currentQuantity(useQuantity - 1, false);
    }
  };

  const onChange = (e: any) => {
    const number = new RegExp(NUM_REGEX);
    const backKey = 'deleteContentBackward';

    if (backKey === e.nativeEvent.inputType && useQuantity == 1) {
      setUseQuantity(0);
      return;
    }

    if (Number(e.target.value) < 1) {
      setTimeout(() => {
        setUseQuantity(1);
        currentQuantity(1, true);
      }, 100);
      return;
    }

    if (e.target.validity.valid && number.test(e.target.value)) {
      setUseQuantity(Number(e.target.value));
      currentQuantity(Number(e.target.value), true);
    }
  };

  return (
    <>
      <div className="container-itemcounter">
        <Button className="buttonleft-itemcounter" onClick={() => onClick('-')}>
          -
        </Button>
        <input
          type="number"
          value={useQuantity}
          className="input-itemcounter"
          onChange={onChange}
          pattern="[0-9]{0,4}"
        />
        <Button
          className="buttonright-itemcounter"
          onClick={() => onClick('+')}
        >
          +
        </Button>
      </div>
    </>
  );
};
