import { UserOutlined } from '@ant-design/icons';

export const CardTestimonial = () => {
  return (
    <div className="flex aspect-square h-[15rem] flex-col rounded-3xl p-5 shadow-sm shadow-slate-400">
      <UserOutlined className="text-3xl" />
      <p className="text-center font-bold uppercase">DAVID CLAVIJEROS</p>
      <p className="text-center font-bold text-slate-400">
        <small>DUEÑO DE GO.CHART</small>
      </p>
      <p className="mt-auto">
        Pagos seguros y productos de calidad. Los recomendaría sin dudarlo.
        Gracias y volveré a comprar con ustedes.
      </p>
    </div>
  );
};
