import { Role } from './Enums';

export const Menu: { label: string; href: string; roles: string[] }[] = [
  {
    label: 'USOS',
    href: '/applications',
    roles: [Role.adminRoot, Role.client, Role.public, Role.admin]
  },
  {
    label: 'TELAS',
    href: '/fabrics',
    roles: [Role.adminRoot, Role.client, Role.public, Role.admin]
  },
  {
    label: 'CONTACTO',
    href: '/contact',
    roles: [Role.client, Role.public]
  },
  {
    label: 'ADMINISTRACIÓN',
    href: '/admin/home',
    roles: [Role.adminRoot, Role.admin]
  }
];

export const sellAdmin: { label: string; key: string }[] = [
  { label: 'Mas vendidas', key: 'mostSold' },
  { label: 'Agotadas', key: 'soldOut' }
];
