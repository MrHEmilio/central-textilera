import React from 'react';

export const HeaderCart = () => {
  return (
    <>
      <div className=" hidden grid-cols-4 sm:grid">
        <div className="header-col ml-10 text-center">Imagen</div>
        <div className="header-col text-center">Nombre</div>
        <div className="header-col mr-5 text-center">Cantidad</div>
        <div className="header-col text-center">Total</div>
      </div>
    </>
  );
};
