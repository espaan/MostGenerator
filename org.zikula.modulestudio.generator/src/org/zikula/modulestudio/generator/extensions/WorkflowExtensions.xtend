package org.zikula.modulestudio.generator.extensions

import com.google.inject.Inject
import de.guite.modulestudio.metamodel.modulestudio.Application
import de.guite.modulestudio.metamodel.modulestudio.Entity
import de.guite.modulestudio.metamodel.modulestudio.EntityWorkflowType
import de.guite.modulestudio.metamodel.modulestudio.ListField
import de.guite.modulestudio.metamodel.modulestudio.ListFieldItem
import java.util.ArrayList

class WorkflowExtensions {

    /**
     * Extensions related to the model layer.
     */
    @Inject extension ModelExtensions = new ModelExtensions()

    /**
     * Determines whether any entity in the given application uses a certain workflow type.
     */
    def hasWorkflow(Application it, EntityWorkflowType wfType) {
        !getEntitiesForWorkflow(wfType).isEmpty
    }

    /**
     * Returns all entities using the given workflow type.
     */
    def getEntitiesForWorkflow(Application it, EntityWorkflowType wfType) {
        getAllEntities.filter(e|e.workflow == wfType)
    }

    /**
     * Checks whether any entity has another workflow than none.
     */
    def needsApproval(Application it) {
        hasWorkflow(EntityWorkflowType::STANDARD) || hasWorkflow(EntityWorkflowType::ENTERPRISE)
    }

    /**
     * Returns all states using by ANY entity using the given workflow type.
     */
    def getRequiredStateList(Application it, EntityWorkflowType wfType) {
        var states = new ArrayList<ListFieldItem>
        var stateIds = new ArrayList<String>
        for (entity : getEntitiesForWorkflow(wfType)) {
            for (item : entity.getWorkflowStateField.items) {
                if (!stateIds.contains(item.value)) {
                    states.add(item)
                    stateIds.add(item.value)
                }
            }
        }
        states
    }

    /**
     * Returns all states using by ANY entity using any workflow type.
     */
    def getRequiredStateList(Application it) {
        var states = new ArrayList<ListFieldItem>
        var stateIds = new ArrayList<String>
        for (entity : getAllEntities) {
            for (item : entity.getWorkflowStateField.items) {
                if (!stateIds.contains(item.value)) {
                    states.add(item)
                    stateIds.add(item.value)
                }
            }
        }
        states
    }

    /**
     * Determines whether any entity in the given application using a certain workflow can have the given state.
     */
    def hasWorkflowState(Application it, EntityWorkflowType wfType, String state) {
        hasWorkflow(wfType) && !getEntitiesForWorkflow(wfType).filter(e|e.hasWorkflowState(state)).isEmpty
    }

    /**
     * Determines whether any entity in the given application can have the given state.
     */
    def hasWorkflowState(Application it, String state) {
        !getAllEntities.filter(e|e.hasWorkflowState(state)).isEmpty
    }

    /**
     * Prints an output string corresponding to the given workflow type.
     */
    def textualName(EntityWorkflowType wfType) {
        switch wfType {
            case EntityWorkflowType::NONE                        : 'none'
            case EntityWorkflowType::STANDARD                    : 'standard'
            case EntityWorkflowType::ENTERPRISE                  : 'enterprise'
            default: ''
        }
    }

    /**
     * Prints an output string regarding the approvals needed by a certain workflow type.
     */
    def approvalType(EntityWorkflowType wfType) {
        switch wfType {
            case EntityWorkflowType::NONE                        : 'no'
            case EntityWorkflowType::STANDARD                    : 'single'
            case EntityWorkflowType::ENTERPRISE                  : 'double'
            default: ''
        }
    }

    /**
     * Returns the list field storing the possible workflow states for the given entity. 
     */
    def getWorkflowStateField(Entity it) {
        fields.filter(typeof(ListField)).filter(e|e.name == 'workflowState').head
    }

    /**
     * Determines whether the given entity has the given workflow state or not.
     */
    def hasWorkflowState(Entity it, String state) {
        !getWorkflowStateItems(state).isEmpty
    }

    /**
     * Determines whether the given entity has the given workflow state or not.
     */
    def getWorkflowStateItem(Entity it, String state) {
        getWorkflowStateItems(state).head
    }

    /**
     * Determines whether the given entity has the given workflow state or not.
     */
    def private getWorkflowStateItems(Entity it, String state) {
        getWorkflowStateField.items.filter(e|e.value == state.toLowerCase)
    }

    /**
     * Returns the description for a given workflow action.
     */
    def getWorkflowActionDescription(EntityWorkflowType wfType, String actionTitle) {
        switch (actionTitle) {
            case 'Defer':               return 'Defer content for later submission.'
            case 'Submit':              return if (wfType == EntityWorkflowType::NONE) 'Submit content.' else 'Submit content for acceptance by a moderator.'
            case 'Update':              return 'Update content.'
            case 'Reject':              return 'Reject content and require improvements.'
            case 'Accept':              return 'Accept content for editors approval.'
            case 'Approve':             return 'Update content and approve for immediate publishing.'
            case 'Submit and Accept':   return 'Submit content and accept immediately.'
            case 'Submit and Approve':  return 'Submit content and approve immediately.'
            case 'Demote':              return 'Disapprove content.'
            case 'Unpublish':           return 'Hide content temporarily.'
            case 'Publish':             return 'Make content available again.'
            case 'Archive':             return 'Move content into the archive'
            case 'Trash':               return 'Move content into the recycle bin.'
            case 'Recover':             return 'Recover content from the recycle bin.'
            case 'Delete':              return 'Delete content permanently.'
        }
        return ''
    }
}