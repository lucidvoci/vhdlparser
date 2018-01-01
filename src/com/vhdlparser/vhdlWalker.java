package com.vhdlparser;

import sun.jvm.hotspot.debugger.cdbg.Sym;

import java.io.IOException;
import java.util.*;

public class vhdlWalker extends vhdlBaseListener {

    Code code = null;
    Process process = null;
    String srcFile;

    Boolean in_sensitivity_list = false;
    Boolean in_interface_port_list = false;
    Boolean in_process = false;
    Boolean in_identifier_list = false;
    Boolean in_relation = false;
    Boolean in_signal_assignment_statement = false;
    Boolean in_waveform = false;
    Boolean in_target = false;

    String identifier_tmp;
    private boolean in_div_or_rem;
    private boolean in_sig_decl;
    private List<String> currently_declared_signals = null;
    private Symbol.Subtype current_subtype = null;
    private boolean in_subtype_decl;
    private boolean in_explicit_range;
    private Integer simple_expression_cnt = null;
    private Integer current_range_min = null;
    private Integer current_range_max = null;
    private boolean in_name_part;
    private Symbol current_symbol = null;
    private boolean in_type_decl;
    private boolean in_type_def;
    private String currently_declared_type;
    private boolean in_constrained_array_def;
    private List<Integer> current_ranges;
    private Integer index_constraint_cnt = null;
    private String current_customtype;

    public vhdlWalker (String srcFile) {
        this.srcFile = srcFile;
    }

    @Override
    public void enterDesign_file(vhdlParser.Design_fileContext ctx) {
        code = Code.getInstance();
        code.srcFile = srcFile;
    }

    @Override
    public void exitDesign_file(vhdlParser.Design_fileContext ctx) {
        //code.printProcesses();
        code.generateAnnotations();
        //code.printAnnotations();
        code.generateOutput();
    }

    @Override
    public void enterProcess_statement(vhdlParser.Process_statementContext ctx) {
        process = new Process(ctx.getText());
        process.lineNumber = ctx.getStart().getLine();
        in_process = true;
    }

    @Override
    public void exitProcess_statement(vhdlParser.Process_statementContext ctx) {
        in_process = false;
        code.addProcess(process);
    }

    @Override
    public void enterName_part(vhdlParser.Name_partContext ctx) {
        if (ctx.selected_name().getText().equals("rising_edge") || ctx.selected_name().getText().equals("falling_edge")) {
            process.clocked = true;
            for (vhdlParser.Name_part_specificatorContext part : ctx.name_part_specificator()){
                if (process.clockName == null) {
                    process.clockName = part.name_function_call_or_indexed_part().actual_parameter_part().getText();
                }
                else if (!process.equals(part.name_function_call_or_indexed_part().actual_parameter_part().getText())){
                    process.annotations.add(new Annotation("-- psl always false // Process should not be clocked by more than one signal.", ctx.getStart().getLine(), srcFile));
                }

            }
        }

        in_name_part = true;
    }

    @Override
    public void enterSelected_name(vhdlParser.Selected_nameContext ctx) {
        if (ctx.getText().equals("rising_edge")) {
            process.clocked = true;
        }
    }


    @Override
    public void enterSignal_mode(vhdlParser.Signal_modeContext ctx) {
        if (ctx.getText().equals("in")) {
            code.addInput(identifier_tmp);
        } else if (ctx.getText().equals("out")) {
            code.addOutput(identifier_tmp);
        }
    }

    @Override
    public void enterIdentifier(vhdlParser.IdentifierContext ctx) {
        String id = ctx.getText();

        if (in_interface_port_list && in_identifier_list) {
            identifier_tmp = id;
        }

        if (in_sig_decl) {
            if (in_identifier_list)
                currently_declared_signals.add(id);

            if (in_subtype_decl) {
                String subtype = id;

                //System.out.println(id);

                switch (subtype) {
                    case "std_logic_vector":
                        current_subtype = Symbol.Subtype.VECTOR;
                        current_customtype = null;
                        break;
                    case "std_logic":
                        current_subtype = Symbol.Subtype.SCALAR;
                        current_customtype = null;
                        break;
                    default:
                        Symbol symbol = SymbolTable.get().lookUp(subtype);
                        if (symbol != null && symbol.getType() == Symbol.Type.TYPE) {
                            current_subtype = Symbol.Subtype.CUSTOM;
                            current_customtype = subtype;
                        }
                        else
                        {
                            current_subtype = null;
                            current_customtype = null;
                        }
                        break;
                }
            }
        }
        
        if (in_type_decl) {
            if (!in_type_def)
                currently_declared_type = id;
        }

        if (in_name_part) {
            current_symbol = SymbolTable.get().lookUp(ctx.getText());
        }

        if (!in_process) { return; }

        if (in_sensitivity_list) {
            process.sensitivityList.add(id);
        }

        if ((in_waveform || in_relation)) {

            if (!process.sensitivityList.contains(id) && code.inputPorts.contains(id) && !process.clocked) {
                process.annotations.add(new Annotation("-- psl always false // '" + id + "' controls output yet it is not in sensitivity list", ctx.getStart().getLine(), srcFile));
            }
            if (code.outputPorts.contains(id)) {
                process.annotations.add(new Annotation("-- psl always false // The output port '" + id + "' is being read from", ctx.getStart().getLine(), srcFile));
                process.outputCorrect = false;
            }
        }

        if (in_target) {
            process.outputPortsUsed.add(id);
        }
    }

    @Override
    public void enterSensitivity_list(vhdlParser.Sensitivity_listContext ctx) {
        in_sensitivity_list = true;
    }

    @Override
    public void exitSensitivity_list(vhdlParser.Sensitivity_listContext ctx) {
        in_sensitivity_list = false;
    }

    @Override
    public void enterInterface_port_list(vhdlParser.Interface_port_listContext ctx) {
        in_interface_port_list = true;
    }

    @Override
    public void exitInterface_port_list(vhdlParser.Interface_port_listContext ctx) {
        in_interface_port_list = false;
    }

    @Override
    public void enterIdentifier_list(vhdlParser.Identifier_listContext ctx) {
        in_identifier_list = true;
    }

    @Override
    public void exitIdentifier_list(vhdlParser.Identifier_listContext ctx) {
        in_identifier_list = false;
    }

    @Override
    public void enterRelation(vhdlParser.RelationContext ctx) {
        in_relation = true;
    }

    @Override
    public void exitRelation(vhdlParser.RelationContext ctx) {
        in_relation = false;
    }

    @Override
    public void enterSignal_assignment_statement(vhdlParser.Signal_assignment_statementContext ctx) {
        in_signal_assignment_statement = true;
    }

    @Override
    public void exitSignal_assignment_statement(vhdlParser.Signal_assignment_statementContext ctx) {
        in_signal_assignment_statement = false;
    }

    @Override
    public void enterWaveform(vhdlParser.WaveformContext ctx) {
        in_waveform = true;
    }

    @Override
    public void exitWaveform(vhdlParser.WaveformContext ctx) {
        in_waveform = false;
    }

    @Override
    public void enterTarget(vhdlParser.TargetContext ctx) {
        in_target = true;
    }

    @Override
    public void exitTarget(vhdlParser.TargetContext ctx) {
        in_target = false;
    }

    @Override
    public void exitFactor(vhdlParser.FactorContext ctx) {
        if (in_div_or_rem) {
            process.annotations.add(new Annotation(String.format("-- psl always (%s /= (others => '0'))", ctx.getText()), ctx.getStart().getLine(), srcFile));
        }

        in_div_or_rem = false;
    }

    @Override
    public void enterMultiplying_operator(vhdlParser.Multiplying_operatorContext ctx) {
        final String[] DIVISION = new String[] { "/", "REM", "MOD" };

        in_div_or_rem = Arrays.asList(DIVISION).contains(ctx.getText());
    }

    @Override
    public void enterSignal_declaration(vhdlParser.Signal_declarationContext ctx) {
        in_sig_decl = true;
        currently_declared_signals = new ArrayList<>();
    }

    @Override
    public void exitSignal_declaration(vhdlParser.Signal_declarationContext ctx) {
        //System.out.println("Declared:");
        //System.out.println(String.join(", ", currently_declared_signals));
        //System.out.println("as");
        //System.out.println(current_subtype);
        //System.out.println("customtype");
        //System.out.println(current_customtype);
        //System.out.println("range");
        //System.out.println(current_range_min);
        //System.out.println(current_range_max);

        if (current_subtype == Symbol.Subtype.VECTOR) {
            currently_declared_signals.stream().forEach(x -> SymbolTable.get().putSignalVector(x,
                    current_range_min, current_range_max));
        }
        else if (current_subtype == Symbol.Subtype.SCALAR) {
            currently_declared_signals.stream().forEach(x -> SymbolTable.get().putSignalScalar(x));
        }
        else if (current_subtype == Symbol.Subtype.CUSTOM && current_customtype != null) {
            currently_declared_signals.stream().forEach(x -> SymbolTable.get().putSignalCustom(x, current_customtype));
        }

        in_sig_decl = false;
        currently_declared_signals = null;
        current_subtype = null;
        current_customtype = null;
        current_range_min = null;
        current_range_max = null;
    }

    @Override
    public void enterSubtype_indication(vhdlParser.Subtype_indicationContext ctx) {
        in_subtype_decl = true;
    }

    @Override
    public void exitSubtype_indication(vhdlParser.Subtype_indicationContext ctx) {
        in_subtype_decl = false;
    }

    @Override
    public void enterExplicit_range(vhdlParser.Explicit_rangeContext ctx) {
        in_explicit_range = true;
        simple_expression_cnt = 0;
    }

    @Override
    public void exitExplicit_range(vhdlParser.Explicit_rangeContext ctx) {
        in_explicit_range = false;
        simple_expression_cnt = null;
    }

    @Override
    public void enterSimple_expression(vhdlParser.Simple_expressionContext ctx) {
        if ((in_subtype_decl || in_type_def) && in_explicit_range) {
            if (simple_expression_cnt != null) {
                if (simple_expression_cnt == 0) {
                    try {
                        current_range_min = Integer.parseInt(ctx.getText());
                    }
                    catch (Exception e) {
                        current_range_min = null;
                    }
                }
                else if (simple_expression_cnt == 1) {
                    try {
                        current_range_max = Integer.parseInt(ctx.getText());
                    }
                    catch (Exception e) {
                        current_range_max = null;
                    }

                    if (current_range_max != null && current_range_min != null &&
                            current_range_min > current_range_max) {
                        Integer tmp = current_range_max;
                        current_range_max = current_range_min;
                        current_range_min = tmp;
                    }
                }

                simple_expression_cnt++;
            }
        }
    }

    @Override
    public void enterName_function_call_or_indexed_part(vhdlParser.Name_function_call_or_indexed_partContext ctx) {
        if (current_symbol == null)
            return;

        if (current_symbol.getType() == Symbol.Type.SIGNAL) {
            if (current_symbol.getSubtype() == Symbol.Subtype.VECTOR) {
                String rhs = ctx.getText();
                process.annotations.add(new Annotation(String.format("-- psl always (%s >= %d) and (%s <= %d)", rhs,
                        current_symbol.getRangeLow(), rhs, current_symbol.getRangeMax()), ctx.getStart().getLine(), srcFile));
            }

            if (current_symbol.getSubtype() == Symbol.Subtype.CUSTOM) {
                String typeName = current_symbol.getCustomTypeId();
                if (typeName != null) {
                    Symbol type = SymbolTable.get().lookUp(typeName);
                    if (type.getSubtype() == Symbol.Subtype.ARRAY && type.getRanges() != null) {
                        Iterator<Integer> iter = type.getRanges().iterator();
                        List<String> clauses = new ArrayList<>();
                        String rhs = ctx.getText();
                        while (iter.hasNext()) {
                            clauses.add(String.format("(%s >= %d) and (%s <= %d))", rhs, iter.next(), rhs, iter.next()));
                        }

                        process.annotations.add(new Annotation("-- psl always " + String.join(" or ", clauses),
                                ctx.getStart().getLine(), srcFile));
                    }
                }

            }
        }

        current_symbol = null;
    }

    @Override
    public void exitName_part(vhdlParser.Name_partContext ctx) {
        in_name_part = false;
    }

    @Override
    public void enterType_declaration(vhdlParser.Type_declarationContext ctx) {
        in_type_decl = true;

        current_ranges = new ArrayList<>();
        current_range_min = null;
        current_range_max = null;
        index_constraint_cnt = 0;
    }

    @Override
    public void exitType_declaration(vhdlParser.Type_declarationContext ctx) {
        in_type_decl = false;

        //System.out.println(currently_declared_type);
        //System.out.println(current_ranges);
        SymbolTable.get().putTypeArray(currently_declared_type, current_ranges);

        currently_declared_type = null;
        current_ranges = null;
        index_constraint_cnt = null;
    }

    @Override
    public void enterType_definition(vhdlParser.Type_definitionContext ctx) {
        in_type_def = true;
    }

    @Override
    public void exitType_definition(vhdlParser.Type_definitionContext ctx) {
        in_type_def = false;
    }
    
    @Override
    public void enterConstrained_array_definition(vhdlParser.Constrained_array_definitionContext ctx) {
        in_constrained_array_def = true;
    }

    @Override
    public void exitConstrained_array_definition(vhdlParser.Constrained_array_definitionContext ctx) {
        in_constrained_array_def = false;
    }

    @Override
    public void exitIndex_constraint(vhdlParser.Index_constraintContext ctx) {
        if (index_constraint_cnt != null)
            index_constraint_cnt++;
    }
    
    @Override
    public void exitDiscrete_range(vhdlParser.Discrete_rangeContext ctx) {
        if (in_type_def && in_constrained_array_def && index_constraint_cnt == 0 &&
                current_range_min != null && current_range_max != null) {
            current_ranges.add(current_range_min);
            current_ranges.add(current_range_max);
        }
    }
}




