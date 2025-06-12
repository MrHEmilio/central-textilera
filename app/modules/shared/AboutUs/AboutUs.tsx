import styles from './AboutUs.module.css';

export const AboutUs = () => {
  return (
    <div className={styles.ConteinerAbout}>
      <div className={styles.ConteinerOverlay}>
        <div className={styles['ConteinerAbout-Body']}>
          <h2 className={styles.Title}>¿Quiénes Somos?</h2>
          <p>
            Somos la distribuidora textil líder en el mercado nacional, contamos
            con la variedad de telas más extensa y de mayor calidad en México.
            Más de 20 años de experiencia nos respaldan. Nuestro objetivo y
            prioridad, es brindarte los mejores productos del mercado, con
            precios altamente competitivos, entregándote el mundo textil con un
            excelente servicio y en la comodidad de tu negocio. ¡Estamos a un
            click de distancia!
          </p>
        </div>
      </div>
    </div>
  );
};
