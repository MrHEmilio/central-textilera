export interface AdminResponseAdmin {
  role: string;
  info: InfoAdmin;
  permission: string[];
  menus: MenuAdmin[];
}

export interface InfoAdmin {
  id: string;
  name: string;
  firstLastname: string;
  secondLastname: null;
  email: string;
  root: boolean;
  active: boolean;
}

export interface MenuAdmin {
  name: string;
  icon: null;
  path: string;
}

export interface ResponseCreateAdmin {
  data: InfoAdmin;
  message: string;
}
