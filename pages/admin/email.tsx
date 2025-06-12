import { NextPage } from 'next';
import { useRef } from 'react';
import { SingleEmailTemplate } from '../../app/interfaces/Response/Admin/EmailServicesResponses';
import { EmailAdminTable } from '../../app/models/TableAdmin/Emails';
import { ProtectAdmin } from '../../app/modules/Admin';
import ModalAddEmailTemplate, {
  ModalEmailTemplateRef
} from '../../app/modules/Admin/ModalAddEmailTemplate';
import TableAdmin from '../../app/modules/Admin/TableAdmin';
import { AdminLayout } from '../../app/modules/shared/AdminLayout';
import { SelectEmailTemplate } from '../../app/services/redux/actions/EmailTemplatesActions';
import { CtxDispatch } from '../../app/services/redux/store';

const Emails: NextPage = () => {
  const modalTemplateRef = useRef<ModalEmailTemplateRef>(null);
  const dispatch = CtxDispatch();
  const onRowSelect = (row: SingleEmailTemplate) => {
    dispatch(SelectEmailTemplate(row));
    modalTemplateRef.current?.toogleVisible(true);
  };
  return (
    <AdminLayout title={'Administración'} pageDescription={''}>
      <ProtectAdmin>
        <div className="color-main container mx-auto ">
          <div className="flex items-center justify-between px-4">
            <h1 className="color-main pb-5 pt-4 text-3xl">Correos</h1>

            <ModalAddEmailTemplate ref={modalTemplateRef} />
          </div>
          <div className="">
            <TableAdmin
              colums={EmailAdminTable}
              onCickRow={onRowSelect}
              url={'email/newsletter/template'}
            />
          </div>
        </div>
      </ProtectAdmin>
    </AdminLayout>
  );
};

export default Emails;
