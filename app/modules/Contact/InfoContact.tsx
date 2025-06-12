import React from 'react';
import style from '/styles/Contact.module.css';
import email from '/public/img/iEmail_azul.svg';
import calendar from '/public/img/iHorario_azul.svg';

export const InfoContact = () => {
  return (
    <div>
      <p className={style.TextBody}>
        ¿Tienes alguna duda? Comunícate con nosotros:
      </p>
      {/*<div className={style.conteinerInfo}>
        <img className="aspect-square h-6 " src={cell.src} />
        <p>55 5862 8640</p>
      </div>*/}
      <div className={style.conteinerInfo}>
        <img className="aspect-square h-6 " src={calendar.src} />
        <div>
          <p>Lunes a Viernes de 10 am a 5 pm</p>
          <p>Sábado de 10 am a 2 pm</p>
        </div>
      </div>
      <div className={style.conteinerInfo}>
        <img className="aspect-square h-6 " src={email.src} />
        <p>cotizacion@centraltextilera.com</p>
      </div>
    </div>
  );
};
