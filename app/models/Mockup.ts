export const cart = [
  {
    name: 'Alaska Extra',
    active: true,
    img: 'https://textile-center-services.com/images/cloths/a4fb8765-bd31-49b5-98d4-de95059d90b0.jpg'
  },
  {
    name: 'Alaska Light',
    active: true,
    img: 'https://textile-center-services.com/images/cloths/a4fb8765-bd31-49b5-98d4-de95059d90b0.jpg'
  }
];

export const filterColor: any[] = [
  { color: 'acero', Label: 'Acero' },
  { color: 'agua', Label: 'Agua' },
  { color: 'aguacate', Label: 'Aguacate' },
  { color: 'amarillo', Label: 'Amarillo' },
  { color: 'amarilloNeon', Label: 'AmarilloNeon' },
  { color: 'arandano', Label: 'Arandano' },
  { color: 'arena', Label: 'Arena' },
  { color: 'azulPetroleo', Label: 'AzulPetroleo ' },
  { color: 'blackOlive', Label: 'Black Olive' },
  { color: 'blanco', Label: 'Blanco' },
  { color: 'botella', Label: 'Botella' },
  { color: 'bronce', Label: 'Bronce' },
  { color: 'bugambilia', Label: 'Bugambilia' },
  { color: 'cafe', Label: 'Cafe' },
  { color: 'camello', Label: 'Camello' }
];

export const colors = [
  { label: 'rojo', color: '#ff0000' },
  { label: 'negro', color: '#000000' },
  { label: 'azul', color: '#001fff' },
  { label: 'verde', color: '#00ff0f' },
  { label: 'hueso', color: '#e7d3af' },
  { label: 'huesos', color: '#e7d3bf' },
  { label: 'canario', color: '#f7d117' }
];

export const mockArr = (items: number): number[] => {
  const arr = Array(items).fill(0);
  return arr;
};
