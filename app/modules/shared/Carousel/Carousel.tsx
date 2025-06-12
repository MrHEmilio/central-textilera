import { Carousel } from 'antd';
import { LeftOutlined, RightOutlined } from '@ant-design/icons';
import { FC, useRef, useState } from 'react';
import { CarouselRef } from 'antd/lib/carousel';
import { getAllBanner } from '../../../interfaces/Response/Banner';

interface Props {
  banners: getAllBanner[];
}

export const CarouselCtx: FC<Props> = ({ banners }) => {
  const [hover, setHover] = useState(false);
  const [i, setI] = useState(0);
  const [speed, setSpeed] = useState(banners[0]?.waitTime * 1000 || 3000);
  const [flag, setFlag] = useState(false);

  const change = () => {
    let index = i;
    if (!banners[index]) return;
    if (!flag) {
      index = i + 1;
      setFlag(true);
      setSpeed(banners[index].waitTime * 1000);
      setI(index + 1);
      return;
    } else {
      if (index === banners.length - 1) {
        setI(0);
        setSpeed(banners[index].waitTime * 1000);
        return;
      }
      setSpeed(banners[index].waitTime * 1000);
      index = i + 1;
      setI(index);
    }
  };
  const toggleHover = () => {
    setHover(!hover);
  };

  const arrowStyle = () => {
    let linkStyle;
    if (hover) {
      linkStyle = { color: '#1890ff' };
    } else {
      linkStyle = { color: 'black' };
    }

    return linkStyle;
  };

  const slider = useRef<CarouselRef>();
  const next = () => {
    if (slider.current) {
      slider.current.next();
    }
  };
  const previous = () => {
    if (slider.current) {
      slider.current.prev();
    }
  };
  return (
    <div style={{ position: 'relative' }}>
      <RightOutlined
        className="hidden md:block"
        onClick={next}
        style={{
          position: 'absolute',
          zIndex: '10',
          top: '40%',
          right: '25px',
          fontSize: '2rem',
          ...arrowStyle()
        }}
        onMouseEnter={toggleHover}
        onMouseLeave={toggleHover}
      />
      <Carousel
        autoplay={true}
        autoplaySpeed={speed}
        ref={ref => {
          if (ref !== undefined && ref) {
            slider.current = ref;
          }
        }}
        beforeChange={change}
      >
        {banners.map(banner => {
          return (
            <div key={banner.id}>
              <img
                src={banner.image}
                style={{ width: '100%' }}
                alt={banner.description}
              />
            </div>
          );
        })}
      </Carousel>
      <LeftOutlined
        className="hidden md:block"
        style={{
          position: 'absolute',
          zIndex: '10',
          top: '40%',
          left: '25px',
          fontSize: '2rem',
          ...arrowStyle()
        }}
        onClick={previous}
        onMouseEnter={toggleHover}
        onMouseLeave={toggleHover}
      />
    </div>
  );
};