import { Card } from 'antd';

import { FC } from 'react';
import style from './CadInfo.module.css';

interface Props {
  title?: string | React.ReactNode;
  children: React.ReactNode;
}

export const CardInfo: FC<Props> = ({ title, children }) => {
  const getTitle = () => {
    if (title !== '' && typeof title === 'string') {
      return { title: <p className={style.TitleCard}>{title}</p> };
    } else if (title) {
      return { title: title };
    } else {
      return '';
    }
  };
  return (
    <div className={`${style.ContainerCard}`}>
      <Card bordered={false} className={style.Card} {...getTitle()}>
        {children}
      </Card>
    </div>
  );
};
