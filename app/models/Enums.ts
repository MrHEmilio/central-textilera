/* eslint-disable no-unused-vars */

const baseURL = process.env.NEXT_PUBLIC_BASE_URL;

export enum OrderByEnum {
  asc = 'ASC',
  desc = 'DESC'
}

export enum Role {
  adminRoot = 'ADMIN_ROOT',
  admin = 'ADMIN',
  client = 'CLIENT',
  public = 'PUBLIC'
}

export const EndpointWith409 = [`${baseURL}client`];

export const ArrayRoutesBack = [
  '/directions',
  '/orders',
  '/directions/addDirection',
  '/directions/editDirection'
];

export const MonthsOfYear = [
  'enero',
  'febrero',
  'marzo',
  'abril',
  'mayo',
  'junio',
  'julio',
  'agosto',
  'septiembre',
  'octubre',
  'noviembre',
  'diciembre'
];

export const ArrarWithSearchBar = ['/fabrics', '/applications'];
