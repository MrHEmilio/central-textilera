import { configureStore } from '@reduxjs/toolkit';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import { IFabricsReducer } from '../../interfaces/State/FabricsReducer';
import {
  ClothFormReducer,
  EditDirections,
  EmailTemplateReducer,
  LoaderReducer,
  SessionReducer
} from './reducers';
import { CartReducer } from './reducers/CartReducers';
import { FabricsReducer } from './reducers/FabricsReducer';
import { TicketReducer } from './reducers/TicketReducer';

import { AdminTableReducer } from './reducers/AdminTableReducer';
import { PaymentReducer } from './reducers/PaymentReducer';
import { ProductDetailReducer } from './reducers/ProductReducers';
import { NotRegisterUserReducer } from './reducers/NotRegisterUser';
import { CollectionSelectReducer } from './reducers/CollectionSelectReducer';
import { NewsLetterReducer } from './reducers/NewsLetterReducer';

export interface CTXStore {
  session: { auth: boolean };
  fabrics: IFabricsReducer;
  loadingPage: { loadingPage: boolean };
}
export const ReduxStore = configureStore({
  reducer: {
    session: SessionReducer,
    editDirection: EditDirections,
    fabrics: FabricsReducer,
    cart: CartReducer,
    productDetailReducer: ProductDetailReducer,
    ticket: TicketReducer,
    typeDelivery: PaymentReducer,
    loader: LoaderReducer,
    admintable: AdminTableReducer,
    clothForm: ClothFormReducer,
    notRegisterUser: NotRegisterUserReducer,
    collectionSelect: CollectionSelectReducer,
    emailTemplateSelected: EmailTemplateReducer,
    newsLetterRegistering: NewsLetterReducer
  }
});

export type RootState = ReturnType<typeof ReduxStore.getState>;
export type appDispatch = typeof ReduxStore.dispatch;
export const CtxDispatch: () => appDispatch = useDispatch;
export const CtxSelector: TypedUseSelectorHook<RootState> = useSelector;
