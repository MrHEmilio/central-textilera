import { Col, Row } from 'antd';
import Link from 'next/link';
import { DEPLOYMENT_VERSION } from '../../../models/constants';
import img from '/public/img/CentralTextileraWhite.webp';
import facebook from '/public/img/iFacebook_blanco.svg';
import insta from '/public/img/iInstagram_blanco.svg';

export const Footer = () => {
  return (
    <footer className="footer">
      <Row justify="center" className="container mx-auto py-4" gutter={16}>
        <Col xs={24} md={6} className="grid place-content-center">
          <img className="p-2 sm:mx-auto" src={img.src} />
        </Col>
        <Col xs={24} md={6}>
          <h4>Servicio al cliente</h4>
          <ul>
            <li>
              <Link href="/applications"> Usos</Link>
            </li>
            <li>
              <Link title="Telas" href="/fabrics">
                Telas
              </Link>
            </li>
            <li>
              <Link title="Contacto" href="/contact">
                Contacto
              </Link>
            </li>
          </ul>
        </Col>
        <Col xs={24} md={6}>
          <h4>Legal</h4>
          <div className="flex w-fit flex-col">
            {/* <Link href={'/returns-changes'} className="cursor-pointer">
                <a className="mb-2">Cambios y Devoluciones</a>
              </Link> */}
            <Link href={'/terms-conditions'} className="cursor-pointer">
              <a className="mb-2">Términos y condiciones</a>
            </Link>
            <Link href={'/privacy'} className="cursor-pointer">
              <a>Aviso de privacidad</a>
            </Link>
          </div>
        </Col>
        <Col className="flex h-fit flex-row gap-3" xs={24} md={6}>
          <a
            href="https://www.facebook.com/centraltextilera/"
            target="_blank"
            rel="noopener noreferrer"
            className="cursor-pointer"
          >
            <img className="aspect-square h-[2rem]" src={facebook.src} />
          </a>
          <a
            href="https://www.instagram.com/centraltextileramx/"
            target="_blank"
            rel="noopener noreferrer"
            className="cursor-pointer"
          >
            <img className="aspect-square h-[2rem]" src={insta.src} />
          </a>
          <small>v{DEPLOYMENT_VERSION}</small>
        </Col>
      </Row>
      <div className="bg-footerlight py-4 text-center text-enphasis">
        <span className="font-bold text-main">
          {new Date().getFullYear()} Central Textilera ®
        </span>{' '}
        | Todos los derechos reservados | Prohibida la reproducción total o
        parcial de esta plataforma
      </div>
    </footer>
  );
};
