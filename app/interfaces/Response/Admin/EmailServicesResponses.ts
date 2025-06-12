export interface SingleEmailTemplate {
  id: string;
  name: string;
  subject: string;
  contentHtml: string;
  contentJson: string;
  active: boolean;
}
