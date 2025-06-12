import { CfdiCatalogResponse } from '../../services/Billing/billing-responses';
import { NewAddress } from '../Request/Client/Address';
import { Sampler } from '../Response/Cloth/Cloth';
import { ShippmentProviderResponse } from '../Response/Shippment';

/**
 * @description item del carrito de redux
 * @param priceNormal -  precio unitario sin descuento
 * @param sellPrice -  precio unitario con descuento
 * @param totalSellPrice -  precio total de una misma tela con descuento
 * @param totalSellPriceNormal - precio total de una misma tela sin descuento
 *
 */
export interface itemCartFull {
  quantity: number;
  variant: string;
  nameColor?: string;
  nameTela?: string;
  img?: string;
  amount?: number;
  sellPrice?: number;
  totalSellPrice?: number;
  priceNormal?: number;
  totalSellPriceNormal?: number;
  discount?: number;
  product?: string;
  nameUrl?: string;
  abbreviation?: string;
}

/**
 * @description response del servicio de precios del carrito
 *
 */
export interface itemCartPrices {
  amount?: number;
  sellPrice: number;
  totalSellPrice: number;
  priceNormal: number;
  totalSellPriceNormal: number;
  product: string;
  discount: number;
}

export interface CartState {
  location?: NewAddress;
  products: itemCartFull[];
  loading: boolean;
  samplers: Sampler[];
  taxLocation?: NewAddress;
  deliveryMethod: 'SHIPPING' | 'PICK_UP';
  shippingInfo?: ShippmentProviderResponse;
  requiresTax: boolean;
  cfdi?: CfdiCatalogResponse;
}
