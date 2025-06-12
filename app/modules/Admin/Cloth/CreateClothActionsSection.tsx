import { LeftOutlined } from '@ant-design/icons';
import { Button, Popconfirm } from 'antd';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { toast } from 'react-toastify';
import {
  activateClothService,
  deleteClothService
} from '../../../services/cloth';
import { CtxSelector } from '../../../services/redux/store';
import { CardInfo } from '../../shared';

export const CreateClothActionsSection = () => {
  const router = useRouter();
  const clothToEdit = CtxSelector(s => s.clothForm);
  const onConfirmDelete = async () => {
    if (!clothToEdit) return;
    const res = await deleteClothService(clothToEdit.id as string);
    if (res) {
      toast.success(res.message, { theme: 'colored' });
      router.replace('/admin/cloth');
    }
  };
  const onActivate = async () => {
    if (!clothToEdit) return;
    const r = await activateClothService(clothToEdit.id!);
    if (!r) return;
    router.replace('/admin/cloth');
  };
  return clothToEdit ? (
    <CardInfo>
      <div className="flex items-center gap-6">
        <Link
          href="/admin/cloth"
          className="m-0 border-none font-famBold text-main"
        >
          <a className="button-ctx-delete mb-[7px] flex items-center gap-2 border-[1px] border-red-500">
            <LeftOutlined />
            Cancelar
          </a>
        </Link>
        {clothToEdit.id && !clothToEdit.active && (
          <Button className="button-ctx m-0" onClick={onActivate}>
            Activar
          </Button>
        )}
        {clothToEdit.id && clothToEdit?.active && (
          <Popconfirm
            className="confirm-cloth"
            okText="Aceptar"
            onConfirm={onConfirmDelete}
            cancelText="Cancelar"
            title="¿Estás seguro de querer desactivar la tela?"
          >
            <Button className="button-ctx-delete m-0">Desactivar Tela</Button>
          </Popconfirm>
        )}
      </div>
    </CardInfo>
  ) : (
    <>No Cloth to Edit</>
  );
};
