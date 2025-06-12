import React from 'react';
import img from '/public/img/check-circle.png';

export const SuccesPay = () => {
  return (
    <div
      className="mb-12 w-full pb-16 text-center"
      style={{ borderBottom: '1px solid #CCCCCC ' }}
    >
      <div>
        <div className="flex justify-center">
          <img src={img.src} alt="" />
        </div>
        <p className="text-3xl" style={{ color: '#78B348' }}>
          ¡Tu pago fue exitoso!
        </p>
      </div>
      <div className="flex justify-center">
        <p className="succesPay-text mt-5 w-2/3">
          Gracias por comprar con nosotros. Tu pedido se está procesando.
        </p>
      </div>
    </div>
  );
};
