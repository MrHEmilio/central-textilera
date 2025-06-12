import { forwardRef, useImperativeHandle, useRef } from 'react';
import EmailEditor, { EditorRef } from 'react-email-editor';
import { toast } from 'react-toastify';
import { EmailServices } from '../../../services/admin/emailServices';
import { CtxSelector } from '../../../services/redux/store';

export type EmailConstructorComponentRef = {
  exportHtml: () => void;
};

interface Props {
  name: string;
  subject: string;
  // eslint-disable-next-line no-unused-vars
  changeReady: (isReady: boolean) => void;
  // eslint-disable-next-line no-unused-vars
  saveResult: (saved: boolean) => void;
}
const EmailConstructorComponent = forwardRef(
  ({ changeReady, saveResult, name, subject }: Props, ref) => {
    const templateSelection = CtxSelector(s => s.emailTemplateSelected);
    const emailService = new EmailServices();
    useImperativeHandle(ref, () => ({
      exportHtml
    }));
    const emailEditorRef = useRef<EditorRef>(null);
    const exportHtml = () => {
      emailEditorRef.current?.editor?.exportHtml(data => {
        const { design, html } = data;
        saveEmailTemplate(JSON.stringify(design), html);
      });
    };

    const saveEmailTemplate = async (design: string, html: string) => {
      if (templateSelection?.id) {
        await emailService.updateEmailTemplate({
          id: templateSelection.id,
          name,
          subject,
          contentHtml: html,
          contentJson: design,
          active: true
        });
      } else {
        const r = await emailService.postAddEmailTemplate({
          name,
          subject,
          contentHtml: html,
          contentJson: design
        });
        if (!r) return;

        toast.success(r, { theme: 'colored' });
      }

      saveResult(true);
    };
    const onLoad = () => {
      if (templateSelection?.contentHtml) {
        emailEditorRef.current?.editor?.loadDesign(
          JSON.parse(templateSelection.contentJson)
        );
      }
    };

    const onReady = () => {
      changeReady(true);
    };

    return (
      <>
        <div className="p-3">
          <EmailEditor ref={emailEditorRef} onLoad={onLoad} onReady={onReady} />
        </div>
      </>
    );
  }
);

EmailConstructorComponent.displayName = 'EmailConstructorComponent';

export default EmailConstructorComponent;
