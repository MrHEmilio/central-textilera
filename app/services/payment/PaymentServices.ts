import { toast } from 'react-toastify';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import {
  PaymentCalculateMethod,
  PaymentCalculatePriceRequest,
  PaymentCalculatePriceShippingFullRequest,
  PaymentCheckoutRequest
} from '../../interfaces/Request/payment/PaymentRequests';
import {
  CalculateDelMethCont,
  ShippmentProviderResponse
} from '../../interfaces/Response/Shippment';
import { BaseService } from '../base-service';

export const getPreferenceIdMP = (
  req: PaymentCheckoutRequest
): Promise<{ id: string } | null> => {
  const baseService = new BaseService();
  return baseService.PostRequest<PaymentCheckoutRequest, { id: string }>(
    req,
    '/payment/checkout/mercado/pago'
  );
};

export const calculatePriceShipping = async (
  req: PaymentCalculatePriceRequest

): Promise<ShippmentProviderResponse[] | null> => {
  const baseService = new BaseService();
  return baseService
    .PostRequest<PaymentCalculatePriceRequest, ShippmentProviderResponse[]>(
      req,
      '/payment/calculate/price/shipping'
    )
    .catch((e: { message: string }) => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};


export const calculatePriceShippingFull = async ({
  req
}: {
  req: PaymentCalculatePriceShippingFullRequest;
}) => {
  return new BaseService()
    .PostRequest<
      PaymentCalculatePriceShippingFullRequest,
      ShippmentProviderResponse[]
    >(req, '/shipping/calculate/price')
    .catch((e: { message: string }) => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

/**
 * @description Encargado de crear la guía en la paquetería correspondientes
 * se llama una vez que se haya generado la orden de forma existosa
 */
export const generateShipment = ({ orderId }: { orderId: string }) => {
  return new BaseService()
    .PostRequest<{ order: string }, { message: string }>(
      { order: orderId },
      '/shipping/tracking'
    )
    .catch((e: { message: string }) => {
      toast.error(e.message, { theme: 'colored' });
      return null;
    });
};

export const calculateDeleveryMethod = (
  req: PaymentCalculateMethod
): Promise<PaginationResponse<CalculateDelMethCont[]> | null> => {
  const baseService = new BaseService();
  return baseService.PostRequest<
    PaymentCalculateMethod,
    PaginationResponse<CalculateDelMethCont[]>
  >(req, '/payment/calculate/delivery/method');
};
