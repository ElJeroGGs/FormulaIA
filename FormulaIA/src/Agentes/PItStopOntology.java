package Agentes;

import jade.content.onto.*;
import jade.content.schema.*;

import jade.content.onto.Ontology;

public class PItStopOntology extends Ontology {
    //Vocabulario que se va a utilizar en la ontologia
    public static final String ONTOLOGY_NAME = "PitStop-ontology";
    public static final String WHEEL_SET = "Juego de Neumaticos";
    public static final String CAMBIAR = "Cambiar";
    public static final String WHEEL_SET_NAME = "compuesto";
    public static final String WHEEL_SET_DURATION = "duracion";
    public static final String CAMBIAR_WHEEL_SET = "Cambiar Juego de Neumaticos";
    public static final String PARADA = "Parada en Boxes";


    
    private static PItStopOntology instance = new PItStopOntology();

    public static PItStopOntology getInstance() {
        return instance;
    }

    

    private PItStopOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {
            //Añadir los elementos de la ontologia
            add(new ConceptSchema(WHEEL_SET), Wheel_set.class);
            add(new AgentActionSchema(CAMBIAR), Cambiar.class);
            add(new PredicateSchema(PARADA), Parada.class);
            //Estructura del esquema para el concepto Wheel_set
            ConceptSchema cs = (ConceptSchema) getSchema(WHEEL_SET);
            cs.add(WHEEL_SET_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(WHEEL_SET_DURATION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            //Estructura del esquema para la accion Cambiar
            AgentActionSchema as = (AgentActionSchema) getSchema(CAMBIAR);
            as.add(CAMBIAR_WHEEL_SET, (ConceptSchema) getSchema(WHEEL_SET));

            //Estructura del esquema para el predicado Parada
            PredicateSchema ps = (PredicateSchema) getSchema(PARADA);
            ps.add(PARADA, (ConceptSchema) getSchema(WHEEL_SET));

        } catch (Exception e) {
            e.printStackTrace();

}
    }
}

    
