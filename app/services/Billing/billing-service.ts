import { toast } from 'react-toastify';
import { NewAddress } from '../../interfaces/Request/Client/Address';
import { CartState } from '../../interfaces/State/Cart';
import { BaseService } from '../base-service';
import { generateShipment } from '../payment/PaymentServices';
import { ReduxStore } from '../redux/store';
import { getShoppingCart } from '../utils';
import {
  BillingWithOutAccountRequest,
  BillinWithoutAccountReq,
  GenerateBillingRequest
} from './billing-requests';
import {
  BillingRegimenFisalResponse,
  CfdiCatalogResponse
} from './billing-responses';

export class BillingService {
  private baseService = new BaseService();

  getFiscalRegimen(): Promise<Array<BillingRegimenFisalResponse> | null> {
    return this.baseService.GetRequest<Array<BillingRegimenFisalResponse>>(
      '/billing/fiscal/regimen'
    );
  }

  async generateBilling(req: GenerateBillingRequest): Promise<boolean | void> {
    return this.baseService
      .PostRequest<GenerateBillingRequest, { message: string }>(req, '/billing')
      .then(r => {
        // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
        toast.success(r!.message, { theme: 'colored' });
        return true;
      })
      .catch(e => {
        toast.error(e.message, { theme: 'colored' });
      });
  }

  async generateBillingWithOutAccount(req: BillingWithOutAccountRequest) {
    return this.baseService
      .PostRequest<BillingWithOutAccountRequest, { message: string }>(
        req,
        '/billing/without/account'
      )
      .then(r => {
        toast.success(r?.message, { theme: 'colored' });
        return true;
      })
      .catch(e => {
        toast.error(e.message, { theme: 'colored' });
      });
  }

  async getCfdiUse(rfc: string): Promise<CfdiCatalogResponse[] | null> {
    return this.baseService.GetRequest<CfdiCatalogResponse[]>(
      `/billing/cfdi/use?rfc=${rfc}`
    );
  }

  sendTaxRequirement(orderId: string) {
    const cartInfo: CartState | null = getShoppingCart();
    const { notRegisterUser } = ReduxStore.getState();
    if (( cartInfo?.shippingInfo || notRegisterUser.userAddress ) && cartInfo?.deliveryMethod === 'SHIPPING') {
      generateShipment({ orderId })
      // eslint-disable-next-line no-console
      .catch(e => console.error('Error: shipment tracking =>', e))
    }
    if (!cartInfo?.requiresTax || !cartInfo.cfdi) return;
    if (
      notRegisterUser.userContact &&
      notRegisterUser.billingInfo &&
      notRegisterUser.fiscalRegimen &&
      (notRegisterUser.userAddress || notRegisterUser.taxAddress)
    ) {
      this.generateBillingWithOutAccount({
        order: orderId,
        cfdiUse: cartInfo.cfdi.Value,
        rfc: notRegisterUser.billingInfo.rfc,
        companyName: notRegisterUser.billingInfo.companyName,
        fiscalRegimen: notRegisterUser.fiscalRegimen,
        billingAddress:
          notRegisterUser.userAddress ||
          (notRegisterUser.taxAddress as NewAddress)
      });
    } else {
      this.generateBilling({
        order: orderId,
        cfdiUse: cartInfo.cfdi.Value
      }).catch((e: { id: string; message: string }) => {
        toast.error(
          e.message ||
          'Hubo un error al dar de alta su factura, intente de nuevo más tarde',
          { theme: 'colored' }
        );
      });
    }
  }

  async billingSendEmail(order: string): Promise<boolean | void> {
    return this.baseService
      .PostRequest<{ order: string }, { message: string }>(
        { order: order },
        '/billing/send/email'
      )
      .then(r => {
        // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
        toast.success(r!.message, { theme: 'colored' });
        return true;
      })
      .catch(e => {
        toast.error(e.message);
      });
  }

  async BillingWithoutAccount(
    req: BillinWithoutAccountReq
  ): Promise<boolean | void> {
    return this.baseService
      .PostRequest<BillinWithoutAccountReq, { message: string }>(
        req,
        '/billing/without/account'
      )
      .then(r => {
        // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
        toast.success(r!.message, { theme: 'colored' });
        return true;
      })
      .catch(e => {
        toast.error(e.message, { theme: 'colored' });
      });
  }
}
