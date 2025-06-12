/* eslint-disable react/jsx-no-target-blank */

import { Skeleton } from 'antd';
import { useRouter } from 'next/router';
import { FC, useEffect, useState } from 'react';
import { LOCATION_STORE, MAP_LOCATION_LINK } from '../../../models/constants';

interface Props {
  height?: number;
}

export const Location: FC<Props> = ({ height }) => {
  const [visible, setVisible] = useState(false);
  const mapLink = MAP_LOCATION_LINK;
  const [loaded, setLoaded] = useState<boolean>(false);
  const router = useRouter();
  const { isReady } = router;

  const renderMap = () => {
    if (loaded) return;
    setLoaded(true);
    setTimeout(async () => {
      setVisible(true);
      const L = await import('leaflet');
      const container = L.DomUtil.get('map');
      if (!container) return;
      if (container.classList.contains('leaflet-container')) return;
      const map = L.map('map', {
        center: [19.05000, -99.1268643],
        zoom: 18,
        scrollWheelZoom: false
      });
      map.invalidateSize();
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19
      }).addTo(map);
      const marker = L.marker([19.43439873455768, -99.12708972781004]).addTo(
        map
      );
      marker
        .bindPopup(
          `<a href="${mapLink}" target="_blank" >${LOCATION_STORE}</a>`
        )
        .openPopup();
    }, 800);
  };

  useEffect(() => {
    if (!isReady) return;
    renderMap();
  }, [isReady]);

  return (
    <>
      {(!visible || !loaded) && (
        <Skeleton.Node
          className="w-full"
          style={{ height: `${height}rem` }}
          active
        />
      )}
      <div
        id="map"
        className={` w-full`}
        style={{
          height: `${height}rem` || '30rem'
        }}
      ></div>
    </>
  );
};
