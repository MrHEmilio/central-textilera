import { Info, Menu } from '../Response/auth/ClienInfo';

export interface SessionStore {
  auth: boolean;
  role?: string;
  info?: Info;
  permission?: string[];
  menus?: Menu[];
}
