import { Skeleton } from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { Price, Sale } from '../../interfaces/Response/Cloth/Cloth';

interface Props {
  prices: Price[];
  quantityCloth: number;
  codeColor: string;
  loading: boolean;
  sale: Sale;
}

export const ProductRange: FC<Props> = ({
  prices = [],
  quantityCloth,
  loading,
  codeColor,
  sale
}) => {
  const [price, setPrice] = useState<Price[]>([]);

  useEffect(() => {
    setPrice(prices);
  }, [loading]);

  const circle = () => {
    return (
      <div
        style={{ background: codeColor, border: '1px solid' }}
        className="h-3 w-3 rounded-full border-main"
      />
    );
  };

  return (
    <div className="container-range-product">
      <h4>Rango de Precios</h4>
      <div>
        <table className="table-rangeproducto">
          <thead>
            <tr>
              <th style={{ width: '190px' }}>Cantidad</th>
              <th>Precio por Unidad</th>
            </tr>
          </thead>
          <tbody>
            {prices.map(p => (
              <tr key={p.id}>
                <td className="flex items-center justify-between">
                  <div>
                    {p.lastAmountRange
                      ? `De ${p.firstAmountRange} a ${p.lastAmountRange} ${sale.name}`
                      : `De ${p.firstAmountRange} a mas ${sale.name}`}
                  </div>

                  {p.lastAmountRange
                    ? quantityCloth > p.firstAmountRange - 1 &&
                      quantityCloth < p.lastAmountRange + 1 &&
                      circle()
                    : quantityCloth > p.firstAmountRange - 1 && circle()}
                </td>
                {loading && (
                  <td>
                    <Skeleton.Input active={true} size="small" />
                  </td>
                )}
                {!loading && (
                  <td
                    className={`detail-price ${
                      price.length > 0 ? '' : 'text-sm text-red-700'
                    }`}
                  >
                    {price.length > 0
                      ? `$${p.price}.00 MXN`
                      : 'No se pudo cargar el precio'}
                  </td>
                )}
              </tr>
            ))}

            {/* <tr>
              <td className="flex items-center justify-between">
                <div>De 10 a 99 metros</div>
                {quantityCloth > 9 && quantityCloth < 100 && circle()}
              </td>
              {loading && (
                <td>
                  <Skeleton.Input active={true} size="small" />
                </td>
              )}
              {!loading && (
                <td
                  className={`detail-price ${
                    price.length > 0 ? '' : ' text-sm text-red-700'
                  }`}
                >
                  {price.length > 0
                    ? `$${price[1].price}.00 MXN`
                    : 'No se pudo cargar el precio'}
                </td>
              )}
            </tr>
            <tr>
              <td className="flex items-center justify-between">
                <div>De 100 a mas metros</div>
                {quantityCloth > 99 && circle()}
              </td>
              {loading && (
                <td>
                  <Skeleton.Input active={true} size="small" />
                </td>
              )}
              {!loading && (
                <td
                  className={`detail-price ${
                    price.length > 0 ? '' : ' text-sm text-red-700'
                  }`}
                >
                  {price.length > 0
                    ? `$${price[2].price}.00 MXN`
                    : 'No se pudo cargar el precio'}
                </td>
              )}
            </tr> */}
          </tbody>
        </table>
      </div>
    </div>
  );
};
