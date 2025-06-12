import { SingleEmailTemplate } from '../../../interfaces/Response/Admin/EmailServicesResponses';
import { EmailTemplateActions } from '../reducers';
import { CtxActions } from './SessionActions';

export const SelectEmailTemplate = (
  template: SingleEmailTemplate
): CtxActions => ({
  type: EmailTemplateActions.set,
  payload: template
});

export const EmailTemplateSetLoading = (loading: boolean): CtxActions => ({
  type: EmailTemplateActions.loading,
  payload: loading
});
