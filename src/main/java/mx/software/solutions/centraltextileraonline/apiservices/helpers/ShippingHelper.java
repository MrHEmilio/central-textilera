package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.IntStream;

import lombok.Data;
import lombok.NoArgsConstructor;
import mx.software.solutions.centraltextileraonline.apiservices.entities.*;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaymentClothVariantRequest;
import mx.software.solutions.centraltextileraonline.apiservices.dtos.MeasureDto;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothVariantRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderRepository;

@Slf4j
@Component
public class ShippingHelper {

    @Autowired
    private ClothVariantRepository clothVariantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    BoxRepository boxRepository;

    //0.05713
    final BigDecimal grosorDefault = new BigDecimal("0.05720");

    public List<MeasureDto> getMeasureDto(final List<PaymentClothVariantRequest> listPaymentClothVariantRequest) {

        final List<MeasureDto> measureDtoList = new ArrayList<>();

        listMesaureDtos(listPaymentClothVariantRequest, measureDtoList);

        return measureDtoList;
    }

    public MeasureDto getMeasureDtoByOrderCloth(final List<OrderClothEntity> listOrderClothEntity) {
        final var measureDto = new MeasureDto();
        listOrderClothEntity.forEach(orderClothEntity -> {
            this.setHight(measureDto, 0.0);
            this.setWidth(measureDto, 0.0);
            this.setWeight(measureDto, 0.0);
        });

        return measureDto;
    }

    public boolean isValidMeasureDto(final MeasureDto measureDto) {
        return (measureDto.getHeight().compareTo(BigDecimal.ZERO) != 0) &&
                (measureDto.getWidth().compareTo(BigDecimal.ZERO) != 0) &&
                (measureDto.getWeight().compareTo(BigDecimal.ZERO) != 0);
    }

    public OrderEntity saveTrackingNumber(final OrderEntity orderEntity, final String trackingNumber) {
        orderEntity.getOrderShippingEntity().setTrackingNumber(trackingNumber);
        this.orderRepository.save(orderEntity);
        return orderEntity;
    }

    public void saveTrackingUrlProvider(final OrderEntity orderEntity, final String trackingUrlProvider) {
        orderEntity.getOrderShippingEntity().setTrackingUrlProvider(trackingUrlProvider);
        this.orderRepository.save(orderEntity);
    }

    public void validateTrackingIdNotExist(final OrderShippingEntity orderShippingEntity) throws ExistException {
        ShippingHelper.log.info("Starting validated the order with the id {} if not exists tracking number.", orderShippingEntity.getOrderEntity().getId());
        if (orderShippingEntity.getTrackingNumber() != null) {
            ShippingHelper.log.info("The order with the id {} has tracking number.", orderShippingEntity.getOrderEntity().getId());
            throw new ExistException(Controller.SHIPPING_TRACKING, "tracking.id", orderShippingEntity.getOrderEntity().getId().toString());
        }
    }

    private void setHight(final MeasureDto measureDto, final double high) {
        BigDecimal highT = BigDecimal.valueOf(high).setScale(2, RoundingMode.HALF_UP);
        measureDto.setHeight(highT);
    }

    private void setLength(final MeasureDto measureDto, final double length) {
        BigDecimal lengthT = BigDecimal.valueOf(length).setScale(2, RoundingMode.HALF_UP);
        measureDto.setLength(lengthT);
    }

    private void setWidth(final MeasureDto measureDto, final double width) {
        BigDecimal ancho = BigDecimal.valueOf(width).setScale(2, RoundingMode.HALF_UP);
        measureDto.setWidth(ancho);
    }

    private void setWeight(final MeasureDto measureDto, final double weigth) {
        BigDecimal peso = BigDecimal.valueOf(weigth).setScale(2, RoundingMode.HALF_UP);
        measureDto.setWeight(peso);
    }

    void listMesaureDtos(final List<PaymentClothVariantRequest> listPaymentClothVariantRequest, final List<MeasureDto> measureDtoList) {
        final String mt = "metro";

        double pesoTotalCorte = 0.0;
        double volumenCorte = 0.0;
        double diamTemp;

        for (PaymentClothVariantRequest item : listPaymentClothVariantRequest) {
            final var optCloth = this.clothVariantRepository.findById(item.getVariant());
            if (optCloth.isPresent()) {
                final var clothEntity = optCloth.get().getClothEntity();
                //Extraer la cantidad de la compra y pasar a double
                final var amout = item.getAmount().doubleValue();
                // Recuperar el diámetro del rollo
                final var dim = clothEntity.getClothMeasureEntity().getDimension().doubleValue();
                // Recuperar medida total por rollo
                final var avRoll = clothEntity.getClothMeasureEntity().getAveragePerRoll().doubleValue();

                final double width;

                if(clothEntity.getClothMeasureEntity().getWidth().doubleValue() > 200){
                    width = clothEntity.getClothMeasureEntity().getWidth().doubleValue() / 2;
                }else{
                    width = clothEntity.getClothMeasureEntity().getWidth().doubleValue();
                }
                // Peso por metro lineal
                final var grXm2 = clothEntity.getClothMeasureEntity().getWeight().doubleValue();
                // Peso gr/mt Lineal
                final var grXmLin = (width / 100) * grXm2;
                //Recuperar la Unidad de medida del material
                final var medSale = clothEntity.getClothBillingEntity().getUnitLabel();

                if (clothEntity.getClothMeasureEntity().getYieldPerKilo() != null) {
                    if (amout >= avRoll) {
                        double[] resultado = splitIntDouble(amout, avRoll);
                        int rolloKg = (int) resultado[0];
                        if (resultado.length > 1) {
                            double restoRollo = resultado[1];
                            if (calculatePercent(restoRollo, avRoll)) {
                                diamTemp = tempDiametro(restoRollo, dim, avRoll);
                                dimPaqts(1, restoRollo, diamTemp, diamTemp, width, measureDtoList);
                            } else {
                                double diam = tempDiametro(restoRollo, dim, avRoll);
                                // volumenCorte += (grosorDefault.doubleValue() * )
                                volumenCorte += volumen(diam, width);
                                pesoTotalCorte += restoRollo;
                            }
                        }
                        dimPaqts(rolloKg, avRoll, dim, dim, width, measureDtoList);
                    } else {
                        if (calculatePercent(amout, avRoll)) {
                            diamTemp = tempDiametro(amout, dim, avRoll);
                            dimPaqts(1, amout, diamTemp, diamTemp, width, measureDtoList);
                        } else {
                            double diam = tempDiametro(amout, dim, avRoll);
                            volumenCorte += volumen(diam, width);
                            pesoTotalCorte += amout;
                        }
                    }//END IF CALCULO DE ROLLOS POR KG
                } else if (medSale.equalsIgnoreCase(mt)) {
                    //Calcular si la cantidad podría ser un rollo
                    if (amout >= avRoll) {
                        double[] resultado = splitIntDouble(amout, avRoll);
                        int cantRollos = (int) resultado[0];

                        if (resultado.length > 1) {
                            double corteTemp = resultado[1];

                            if (calculatePercent(corteTemp, avRoll)) {
                                diamTemp = tempDiametro(corteTemp, dim, avRoll);
                                double peso = tempPeso(corteTemp, grXmLin);
                                dimPaqts(1, peso, diamTemp, diamTemp, width, measureDtoList);
                            } else {
                                volumenCorte += (grosorDefault.doubleValue() * width * corteTemp);
                                pesoTotalCorte += tempPeso(corteTemp, grXmLin);
                            }
                        }
                        double peso = tempPeso(avRoll, grXmLin);
                        dimPaqts(cantRollos, peso, dim, dim, width, measureDtoList);
                    } else {
                        if (calculatePercent(amout, avRoll)) {
                            diamTemp = tempDiametro(amout, dim, avRoll);
                            double peso = tempPeso(amout, grXmLin);
                            dimPaqts(1, peso, diamTemp, diamTemp, width, measureDtoList);
                        } else {
                            // double diam = tempDiametro(amout, dim, avRoll);
                            volumenCorte += (grosorDefault.doubleValue() * width * amout);
                            pesoTotalCorte += tempPeso(amout, grXmLin);
                        }
                    }//End if rollos y corte
                } else {
                    System.out.println("Medida por piezas");
                }//End If Comparación medidas
            }//End If Cloth isPresent
        }//End For List Items

        if (volumenCorte > 0) {
            var boxes = this.boxRepository.findAll();
            List<BoxDto> listBox = new ArrayList<>();
            //Calcular volumen de cada caja
            boxes.forEach(box -> {
                if (box.isActive()) {
                    BigDecimal volumen = volBoxes(box);
                    BoxDto bxDto = new BoxDto();
                    bxDto.setBxEntity(box);
                    bxDto.setVolumen(volumen);
                    listBox.add(bxDto);
                }
            });
            //ordenar la lista por volumen menor a mayor
            listBox.sort(Comparator.comparing(BoxDto::getVolumen));

            //Valores de referencia Máximos
            BoxDto bxMax = listBox.get(listBox.size()-1);

            //if (volumenCorte > volMaximo) {
            if(volumenCorte > bxMax.getVolumen().doubleValue()){
                double[] respose = splitIntDouble(volumenCorte, bxMax.getVolumen().doubleValue());
                int boxs = (int) respose[0];
                if (respose.length > 1) {
                    double restoVolumen = respose[1];
                    double pesoCorte = (restoVolumen * pesoTotalCorte) / volumenCorte;
                    boxImplements(listBox, restoVolumen, pesoCorte, measureDtoList);
                }
                double weightTemp = (bxMax.getVolumen().doubleValue() * pesoTotalCorte) / volumenCorte;
                //Mandar crear las cajas de mayor volumen
                dimPaqts(boxs, weightTemp, bxMax.getBxEntity().getHeight().doubleValue(), bxMax.getBxEntity().getDepth().doubleValue(),
                        bxMax.getBxEntity().getWidth().doubleValue(), measureDtoList);
            } else {
                boxImplements(listBox, volumenCorte, pesoTotalCorte, measureDtoList);
            }
        }//End if suma de todos los cortes > 0
    }

    boolean calculatePercent(double resto, double avRoll) {return ((resto * 100) / avRoll) >= 70;}

    void boxImplements(List<BoxDto> listBox, double volCorte, double pesoCorte, final List<MeasureDto> measureDtoList) {
        for (BoxDto box : listBox) {
            if (box.getBxEntity().getName().equalsIgnoreCase("Rollo") || box.getBxEntity().getName().equalsIgnoreCase("Rollo Mini"))
                continue;
            if (volCorte <= box.getVolumen().doubleValue()) {
                BoxEntity auxBox = box.getBxEntity();
                dimPaqts(1, pesoCorte, auxBox.getHeight().doubleValue(), auxBox.getDepth().doubleValue(),
                        auxBox.getWidth().doubleValue(), measureDtoList);
                break;
            }
        }
    }

    void dimPaqts(int cantidad, double weigth, double height, double length, double width, final List<MeasureDto> measureDtoList) {
        IntStream.rangeClosed(1, cantidad)
                .forEach(i -> {
                    final var measureDto = new MeasureDto();
                    setHight(measureDto, height);
                    setLength(measureDto, length);
                    setWidth(measureDto, width);
                    setWeight(measureDto, weigth);
                    measureDtoList.add(measureDto);
                });
    }

    private double volumen(double diametro, double width) { return (Math.PI * Math.pow((diametro / 2), 2) * width); }

    private BigDecimal volBoxes(final BoxEntity box) { return box.getHeight().multiply(box.getWidth().multiply(box.getDepth())); }

    private double tempDiametro(double amout, double diametro, double avRoll) { return (amout * diametro) / avRoll; }

    private double tempPeso(double amout, double pXmLin) { return (amout * pXmLin) / 1000; }

    private double[] splitIntDouble(double cantidad, double divisor) {
        //Aquí calculo la parte entera y la parte fraccionaria si la cantidad de compra rebasa el tamaño del rollo
        int entero = (int) (cantidad / divisor);
        double resto = cantidad % divisor;
        if (resto > 0) return new double[]{entero, resto};
        return new double[]{entero};
    }
    
}

@Data
@NoArgsConstructor
class BoxDto {
    private BoxEntity bxEntity;
    private BigDecimal volumen = BigDecimal.ZERO;
}
