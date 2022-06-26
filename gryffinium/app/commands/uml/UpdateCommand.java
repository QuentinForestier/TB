package commands.uml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import graphical.GraphicalElementType;
import graphical.entities.*;
import graphical.entities.operations.GraphicalMethod;
import graphical.entities.operations.GraphicalOperation;
import graphical.entities.variables.GraphicalAttribute;
import graphical.entities.variables.GraphicalValue;
import graphical.links.GraphicalBinaryAssociation;
import graphical.links.GraphicalMultiAssociation;
import models.Project;
import play.libs.Json;
import uml.entities.*;
import uml.entities.Class;
import uml.entities.Enum;
import uml.entities.operations.Constructor;
import uml.entities.operations.Method;
import uml.entities.variables.Attribute;
import uml.links.BinaryAssociation;
import uml.links.MultiAssociation;

public class UpdateCommand implements Command
{
    JsonNode data;

    GraphicalElementType elementType;

    public UpdateCommand(JsonNode data, GraphicalElementType elementType)
    {
        this.data = data;
        this.elementType = elementType;
    }

    public void setData(JsonNode data)
    {
        this.data = data;
    }

    public JsonNode getData()
    {
        return data;
    }

    @Override
    public JsonNode execute(Project project)
    {
        ObjectNode result = null;

        switch (elementType)
        {
            case CLASS:
                GraphicalClass ge = Json.fromJson(data,
                        GraphicalClass.class);
                project.getDiagram().getEntity(ge.getId()).setGraphical(ge);
                result = (ObjectNode) Json.toJson(ge);
                break;
            case INNER_CLASS:
                GraphicalInnerClass gic = Json.fromJson(data,
                        GraphicalInnerClass.class);
                project.getDiagram().getEntity(gic.getId()).setGraphical(gic);
                result = (ObjectNode) Json.toJson(gic);
                break;
            case ASSOCIATION_CLASS:
                GraphicalAssociationClass gac =
                        Json.fromJson(data,
                                GraphicalAssociationClass.class);
                project.getDiagram().getEntity(gac.getId()).setGraphical(gac);
                result = (ObjectNode) Json.toJson(gac);
                break;
            case ENUM:
                GraphicalEnum gen = Json.fromJson(data,
                        GraphicalEnum.class);
                project.getDiagram().getEntity(gen.getId()).setGraphical(gen);
                result = (ObjectNode) Json.toJson(gen);
                break;
            case INTERFACE:
                GraphicalEntity gc = Json.fromJson(data,
                        GraphicalEntity.class);
                project.getDiagram().getEntity(gc.getId()).setGraphical(gc);
                result = (ObjectNode) Json.toJson(gc);
                break;
            case INNER_INTERFACE:
                GraphicalInnerInterface gi = Json.fromJson(data,
                        GraphicalInnerInterface.class);
                project.getDiagram().getEntity(gi.getId()).setGraphical(gi);
                result = (ObjectNode) Json.toJson(gi);
                break;


            case BINARY_ASSOCIATION:
            case AGGREGATION:
            case COMPOSITION:
                GraphicalBinaryAssociation gba = Json.fromJson(data,
                        GraphicalBinaryAssociation.class);
                BinaryAssociation ba = new BinaryAssociation(gba,
                        project.getDiagram());
                project.getDiagram().addAssociation(ba);
                result = (ObjectNode) Json.toJson(ba);
                break;
            case MUTLI_ASSOCIATION:
                break;
            case DEPENDENCY:
                break;
            case GENEREALIZATION:
                break;
            case REALIZATION:
                break;
            case INNER:
                break;

            case VALUE:
                GraphicalValue gv = Json.fromJson(data,
                        GraphicalValue.class);

                Enum eParent =
                        (Enum) project.getDiagram().getEntity(gv.getParentId());
                eParent.updateValue(gv.getOldValue(), gv.getValue());
                result = (ObjectNode) Json.toJson(gv);
                break;

            case ATTRIBUTE:
                GraphicalAttribute ga = Json.fromJson(data,
                        GraphicalAttribute.class);

                project.getDiagram().getEntity(ga.getParentId()).getAttribute(ga.getId()).setGraphical(ga, project.getDiagram());
                result = (ObjectNode) Json.toJson(ga);
                break;
            case PARAMETER:
                break;


            case CONSTRUCTOR:
                GraphicalOperation go = Json.fromJson(data,
                        GraphicalOperation.class);

                ConstructableEntity parent =
                        (ConstructableEntity) project.getDiagram().getEntity(go.getParentId());

                parent.getConstructorById(go.getId()).setGraphical(go,
                        project.getDiagram());
                result = (ObjectNode) Json.toJson(go);
                break;
            case METHOD:
                GraphicalMethod gm = Json.fromJson(data,
                        GraphicalMethod.class);
                project.getDiagram().getEntity(gm.getParentId()).getMethodById(gm.getId()).setGraphical(gm, project.getDiagram());
                result = (ObjectNode) Json.toJson(gm);
                break;
        }

        result.put("elementType", elementType.toString());

        return result;
    }
}
