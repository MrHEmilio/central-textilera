import { toast } from 'react-toastify';
import { PaginationResponse } from '../../interfaces/paginationResponse';
import {
  CreateEmailTemplate,
  SendEmailRequest
} from '../../interfaces/Request/Admin/EmailTemplate';
import { SingleEmailTemplate } from '../../interfaces/Response/Admin/EmailServicesResponses';
import { PostResponse } from '../../interfaces/Response/PostResponse';
import { BaseService } from '../base-service';
import { EmailTemplateSetLoading } from '../redux/actions/EmailTemplatesActions';
import { CtxDispatch } from '../redux/store';

export class EmailServices {
  http = new BaseService();
  dispatch = CtxDispatch();
  getEmail(): Promise<PaginationResponse<SingleEmailTemplate[]> | null> {
    return this.http.GetRequest('/email/newletter/template');
  }

  async postAddEmailTemplate(req: CreateEmailTemplate): Promise<string | null> {
    this.dispatch(EmailTemplateSetLoading(true));
    return this.http
      .PostRequest<CreateEmailTemplate, PostResponse<SingleEmailTemplate>>(
        req,
        '/email/newsletter/template'
      )
      .then(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        return r?.message || null;
      })
      .catch(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.error(r.message, { theme: 'colored' });
        return null;
      });
  }

  updateEmailTemplate(req: SingleEmailTemplate): Promise<void | null> {
    this.dispatch(EmailTemplateSetLoading(true));
    return this.http
      .PutRequest<SingleEmailTemplate, PostResponse<SingleEmailTemplate>>(
        req,
        '/email/newsletter/template'
      )
      .then(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.success(
          r?.message || 'Hubo un error al intentar actualizar esta plantilla',
          { theme: 'colored' }
        );
      })
      .catch(err => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.error(err.message, { theme: 'colored' });
      });
  }

  deleteDeactivateEmailTemplate(id: string): Promise<void | null> {
    this.dispatch(EmailTemplateSetLoading(true));
    return this.http
      .DeleteRequest<PostResponse<SingleEmailTemplate>>(
        `/email/newsletter/template/${id}`
      )
      .then(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.success(
          r?.message || 'Hubo un error al intentar crear la plantilla',
          { theme: 'colored' }
        );
      })
      .catch(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.error(r.message, { theme: 'colored' });
      });
  }

  reactiveEmailTemplate(id: string): Promise<void | null> {
    this.dispatch(EmailTemplateSetLoading(true));
    return this.http
      .PutRequest<null, PostResponse<SingleEmailTemplate>>(
        null,
        `/email/newsletter/template/${id}`
      )
      .then(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.success(r?.message, { theme: 'colored' });
      })
      .catch(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.error(r.message, { theme: 'colored' });
      });
  }

  postSendTemplate(req: SendEmailRequest): Promise<void | null> {
    this.dispatch(EmailTemplateSetLoading(true));
    return this.http
      .PostRequest<SendEmailRequest, { id: string; message: string }>(
        req,
        '/email/newsletter/template/send'
      )
      .then(r => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.success(
          r?.message || 'Se ha ejecutado el envío masivo correctamente.',
          { theme: 'colored' }
        );
      })
      .catch(err => {
        this.dispatch(EmailTemplateSetLoading(false));
        toast.error(
          err.message || 'Hubo un error al intentar enviar los correos.',
          { theme: 'colored' }
        );
      });
  }
}
