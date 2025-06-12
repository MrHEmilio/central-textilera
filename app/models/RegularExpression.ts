export const NAME_REGEX = '^[A-Za-zñÑáéíóúÁÉÍÓÚ ]*$';
export const LASTNAME_REGEX = '^[A-Za-zñÑáéíóúÁÉÍÓÚ]*$';
export const NUM_REGEX_LONG10 = '^[0-9]{10}$';
export const NUM_REGEX = '^[0-9]*$';
export const NUM_REGEX_TWO_DECIMALS = /^\d+(\.\d{1,2})?$/;
export const ZIPCODE_REGEX = '^[0-9]{5}$';
export const HEXADECIMAL_COLOR = '^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$';
export const RFC_REGEX =
  /^([A-ZÑ&]{3,4}) ?(?:- ?)?(\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])) ?(?:- ?)?([A-Z\d]{2})([A\d])$/;
