export interface ClientInfo {
  role: string;
  info: Info;
  permission: string[];
  menus: Menu[];
}

export interface Info {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: null;
  phone: string;
  email: string;
  emailValidated: boolean;
  active: boolean;
}

export interface Menu {
  name: string;
  icon: null;
  path: string;
}
