import { Col, Row } from 'antd';

export const SavePay = () => {
  return (
    <div className="save-container">
      <Row gutter={[50, 30]} className="save-body">
        <Col xs={{ span: 24 }} lg={{ span: 6, offset: 2 }} className="col-pay">
          <div className="save-icon ">
            <img src="img/iconSavePay.webp" />
            <p className="icon-text">Pago Seguro</p>
          </div>
        </Col>
        <Col xs={{ span: 24 }} lg={{ span: 6, offset: 2 }} className="col-pay">
          <div className="save-icon ship-icon">
            <img className="save-icon" src="img/shipments.webp" />
            <p className="icon-text">Envíos a toda la república</p>
          </div>
        </Col>
        <Col xs={{ span: 24 }} lg={{ span: 6, offset: 2 }} className="col-pay">
          <div className="save-icon ">
            <img className="save-icon" src="img/stockIcon.webp" />
            <p className="icon-text">Amplio stock de telas</p>
          </div>
        </Col>
      </Row>
    </div>
  );
};
