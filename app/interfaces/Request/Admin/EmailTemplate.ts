export interface CreateEmailTemplate {
  name: string;
  subject: string;
  contentHtml: string;
  contentJson: string;
}
export interface SendEmailRequest {
  emailNewsletterTemplate: string;
  sendTo: string;
}
