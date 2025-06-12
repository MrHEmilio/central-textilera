import { FC, useState, useEffect } from 'react';
import { Tooltip } from 'antd';
import { Variant } from '../../interfaces/Response/Cloth/Cloth';

interface Props {
  variants: Variant[] | undefined;
  // eslint-disable-next-line no-unused-vars
  selectColor: (arg0: string, arg1: string, arg2: string) => void;
}

export const ColorPicker: FC<Props> = ({ variants, selectColor }) => {
  const [variant, setVariant] = useState<Variant[]>([]);
  const [currentColor, setCurrentColor] = useState<string>('');

  const onclick = (
    color: string,
    codeColor: string,
    nombreColor: string,
    idColor: string
  ) => {
    setCurrentColor(idColor);
    selectColor(color, codeColor, nombreColor);
  };

  useEffect(() => {
    if (variants) {
      setVariant(variants);
      setCurrentColor(variants[0].color.id);
      selectColor(
        variants[0].id,
        variants[0].color.code || '',
        variants[0].color.name
      );
    }
  }, [variants]);

  return (
    <div>
      <div className="container-colorpicker">
        {variants &&
          variant.map(color => {
            return (
              <Tooltip
                key={color.color.id}
                placement="topLeft"
                title={color.color.name}
              >
                <div
                  onClick={() =>
                    onclick(
                      color.id,
                      color.color.code || '',
                      color.color.name,
                      color.color.id
                    )
                  }
                  className="item-colorpicker"
                  style={{
                    backgroundColor: color.color.code,
                    border:
                      color.color.id === currentColor
                        ? '2.5px solid blue'
                        : '2.5px solid #898989'
                  }}
                ></div>
              </Tooltip>
            );
          })}
      </div>
    </div>
  );
};
