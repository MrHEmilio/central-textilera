import React from 'react';

export const TrackingHeader = () => {
  return (
    <>
      <div className=" hidden grid-cols-5 sm:grid">
        <div className="header-col ml-10 text-center">Detalle</div>
        <div className="header-col text-start">Nombre</div>
        <div className="header-col text-start ">Paquetes</div>
        <div className="header-col text-start">Pedido realizado</div>
        <div className="header-col mr-10 text-start">Total</div>
      </div>
    </>
  );
};
