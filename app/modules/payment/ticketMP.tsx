import { useEffect} from 'react';
import { CartState } from '../../interfaces/State/Cart';
import img from '/public/img/pagoefectivo.png';
import { useRouter } from 'next/router';
import { toast } from 'react-toastify';
import { NewAddress } from '../../../app/interfaces/Request/Client/Address';
import { NotRegisterUserState } from '../../../app/interfaces/State/NotRegisterUserInterface';
import {
    ClothSimple,
    PaymentMethods,
    SamplerSimple,
    ShippingInterface,
    ShippingNotResiterUser,
    createOrderNotRegisteredUser,
    createOrderService,
} from '../../../app/services';
import { LoaderActionsShow } from '../../../app/services/redux/actions';
import { SessionReducerT } from '../../../app/services/redux/reducers';
import { CtxDispatch, ReduxStore } from '../../../app/services/redux/store';
import { cart } from '../../models';


export const StatusMP = () => {
    const router = useRouter();
    const { isReady } = router;
    
    const dispatch = CtxDispatch();

    useEffect(() => {
        if (!isReady) return;
        const cart: CartState = JSON.parse(localStorage.getItem('cart') || '');
        const preference = String(sessionStorage.getItem('preference'));

        if(!preference){
            return;
        } 
        
        if (!cart.products && !cart.samplers) return;
        const {session, notRegisterUser } = ReduxStore.getState();

        if (session === null || session === undefined) return;

        createOrder(preference as string, cart, notRegisterUser, session);
        dispatch(LoaderActionsShow());
    }, [isReady, cart]);

    const createOrder = (
        paymentId: string,
        cartInfo: CartState,
        notRegisteredUserState: NotRegisterUserState,
        session: SessionReducerT
    ) => {
        const cloths: ClothSimple[] = cartInfo.products.map(i => ({
            amount: i.quantity,
            variant: i.variant
        }));
        const samplers: SamplerSimple[] = cartInfo?.samplers.map(i => ({
            amount: i.quantity || 1,
            sampler: i.id
        }));

        if (notRegisteredUserState && !session?.auth) {
            const { userContact, taxAddress, userAddress } = notRegisteredUserState;
            if (!userContact) return;
            createOrderNotRegisteredUser({
                client: {
                    phone: userContact.phone,
                    name: userContact.name,
                    secondLastname: userContact.secondLastName || '',
                    firstLastname: userContact.lastName,
                    email: userContact.email,
                    countryCode: userContact.countryCode
                },
                billingAddress: (taxAddress ? taxAddress : userAddress) as NewAddress,
                cloths,
                samplers,
                deliveryMethod: cartInfo.deliveryMethod,
                paymentId: paymentId,
                paymentMethod: PaymentMethods.mp,
                ...(cartInfo.deliveryMethod !== 'PICK_UP' && {
                    shipping: {
                        clientAddress: userAddress as NewAddress,
                        price: cartInfo.shippingInfo?.price || '',
                        date: cartInfo.shippingInfo?.date || '',
                        provider: cartInfo.shippingInfo?.provider || '',
                        serviceCode: cartInfo.shippingInfo?.serviceCode || '',
                        serviceName: cartInfo.shippingInfo?.serviceName || '',
                        shippingMethod: cartInfo.shippingInfo?.shippingMethod || '',
                        rateId: cartInfo.shippingInfo?.rateId || ''
                    } as ShippingNotResiterUser
                }),
                addressSame: !taxAddress,
                orderBilling: cartInfo.requiresTax || false
            /* eslint-disable-next-line @typescript-eslint/no-explicit-any*/
            }).then((r:any)=>{
                if(!r) return;
                toast.info(r.message)
            })
            return;
        }
        dispatch(LoaderActionsShow());

        createOrderService({
            paymentId: paymentId,
            deliveryMethod: cartInfo.deliveryMethod,
            samplers,
            shippingAddress: cartInfo.location?.id || undefined,
            billingAddress: cartInfo.taxLocation?.id || undefined,
            cloths: cloths,
            paymentMethod: PaymentMethods.mp,
            ...(cartInfo.deliveryMethod !== 'PICK_UP' && {
                shipping: {
                    ...cartInfo.shippingInfo,
                    clientAddress: cartInfo.location?.id
                } as ShippingInterface
            })
            /* eslint-disable-next-line @typescript-eslint/no-explicit-any*/
        }).then((r: any)=>{
            if(!r) return;
            toast.info(r.message)
        })

        sessionStorage.removeItem('preference');    
    };

    useEffect(() => {
        dispatch(LoaderActionsShow());
    }, []);

    return (
        <div
            className="mb-12 w-full p-16 text-center"
            style={{ borderBottom: '1px solid #CCCCCC ' }}
        >
            <div>
                <p className="text-3xl ps-16 font-bold color-main">
                    ¡Gracias por tu pedido, tu compra esta en Proceso!
                </p>
                <p className="text-3xl ps-16 font-bold color-main">
                </p>
                <div className="flex justify-center">
                    <img src={img.src} alt="pago en proceso" />
                </div>
            </div>
            <div className="flex justify-center">
                <p className="mt-5 w-2/3 text-xl color-main">
                    Una vez confirmado tu pago recibirás un e-mail con información de los detalles de tu compra.
                </p>
            </div>
        </div>
    )

}